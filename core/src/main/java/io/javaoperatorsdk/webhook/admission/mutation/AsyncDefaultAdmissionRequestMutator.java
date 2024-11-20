package io.javaoperatorsdk.webhook.admission.mutation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionRequestHandler;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;
import io.javaoperatorsdk.webhook.clone.Cloner;
import io.javaoperatorsdk.webhook.clone.ObjectMapperCloner;

import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.admissionResponseFromMutation;
import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.getTargetResource;
import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.notAllowedExceptionToAdmissionResponse;

public class AsyncDefaultAdmissionRequestMutator<T extends KubernetesResource>
    implements AsyncAdmissionRequestHandler {

  private final AsyncMutator<T> asyncMutator;
  private final Cloner<T> cloner;

  public AsyncDefaultAdmissionRequestMutator(Mutator<T> mutator) {
    this(mutator, new ObjectMapperCloner<>());
  }

  public AsyncDefaultAdmissionRequestMutator(Mutator<T> mutator, Cloner<T> cloner) {
    this((AsyncMutator<T>) (resource, operation) -> CompletableFuture.supplyAsync(
        () -> mutator.mutate(resource, operation)), cloner);
  }

  public AsyncDefaultAdmissionRequestMutator(AsyncMutator<T> asyncMutator) {
    this(asyncMutator, new ObjectMapperCloner<>());
  }

  public AsyncDefaultAdmissionRequestMutator(AsyncMutator<T> asyncMutator, Cloner<T> cloner) {
    this.asyncMutator = asyncMutator;
    this.cloner = cloner;
  }

  @Override
  @SuppressWarnings("unchecked")
  public CompletionStage<AdmissionResponse> handle(AdmissionRequest admissionRequest) {
    var operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);
    var clonedResource = cloner.clone(originalResource);
    return asyncMutator.mutate(clonedResource, operation)
        .thenApply(resource -> admissionResponseFromMutation(originalResource, resource))
        .exceptionally(e -> {
          if (e instanceof CompletionException) {
            if (e.getCause() instanceof NotAllowedException) {
              return notAllowedExceptionToAdmissionResponse((NotAllowedException) e.getCause());
            }
            throw new IllegalStateException(e.getCause());
          }
          throw new IllegalStateException(e);
        });
  }
}
