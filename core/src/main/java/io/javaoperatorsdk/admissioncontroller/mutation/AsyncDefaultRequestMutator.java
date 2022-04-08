package io.javaoperatorsdk.admissioncontroller.mutation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.*;
import io.javaoperatorsdk.admissioncontroller.clone.Cloner;
import io.javaoperatorsdk.admissioncontroller.clone.ObjectMapperCloner;

import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.admissionResponseFromMutation;
import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.getTargetResource;

public class AsyncDefaultRequestMutator<T extends KubernetesResource>
    implements AsyncRequestHandler {

  private final AsyncMutator<T> mutator;
  private final Cloner<T> cloner;

  public AsyncDefaultRequestMutator(AsyncMutator<T> mutator) {
    this(mutator, new ObjectMapperCloner<>());
  }

  public AsyncDefaultRequestMutator(AsyncMutator<T> mutator, Cloner<T> cloner) {
    this.mutator = mutator;
    this.cloner = cloner;
  }

  @Override
  public CompletionStage<AdmissionResponse> handle(AdmissionRequest admissionRequest) {
    Operation operation = Operation.valueOf(admissionRequest.getOperation());
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
