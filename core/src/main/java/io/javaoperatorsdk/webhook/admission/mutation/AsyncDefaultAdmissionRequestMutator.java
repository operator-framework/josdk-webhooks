package io.javaoperatorsdk.webhook.admission.mutation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.webhook.admission.AdmissionUtils;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionRequestHandler;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;
import io.javaoperatorsdk.webhook.clone.Cloner;
import io.javaoperatorsdk.webhook.clone.ObjectMapperCloner;

import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.admissionResponseFromMutation;
import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.getTargetResource;

public class AsyncDefaultAdmissionRequestMutator<T extends KubernetesResource>
    implements AsyncAdmissionRequestHandler {

  private final AsyncMutator<T> mutator;
  private final Cloner<T> cloner;

  public AsyncDefaultAdmissionRequestMutator(AsyncMutator<T> mutator) {
    this(mutator, new ObjectMapperCloner<>());
  }

  public AsyncDefaultAdmissionRequestMutator(AsyncMutator<T> mutator, Cloner<T> cloner) {
    this.mutator = mutator;
    this.cloner = cloner;
  }

  @Override
  public CompletionStage<AdmissionResponse> handle(AdmissionRequest admissionRequest) {
    var operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);
    var clonedResource = cloner.clone(originalResource);
    CompletionStage<AdmissionResponse> admissionResponse;
    try {
      var mutatedResource = mutator.mutate(clonedResource, operation);
      admissionResponse =
          mutatedResource.thenApply(mr -> admissionResponseFromMutation(originalResource, mr));
    } catch (NotAllowedException e) {
      admissionResponse = CompletableFuture
          .supplyAsync(() -> AdmissionUtils.notAllowedExceptionToAdmissionResponse(e));
    }
    return admissionResponse;
  }
}
