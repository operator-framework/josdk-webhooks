package io.javaoperatorsdk.admissioncontroller.validation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.*;

import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.getTargetResource;

public class AsyncDefaultRequestValidator<T extends KubernetesResource>
    implements AsyncRequestHandler {

  private final Validator<T> validator;

  public AsyncDefaultRequestValidator(Validator<T> validator) {
    this.validator = validator;
  }

  @Override
  @SuppressWarnings("unchecked")
  public CompletionStage<AdmissionResponse> handle(AdmissionRequest admissionRequest) {
    Operation operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);

    var asyncValidate =
        CompletableFuture.runAsync(() -> validator.validate(originalResource, operation));
    return asyncValidate
        .thenApply(v -> allowedAdmissionResponse())
        .exceptionally(e -> {
          if (e instanceof CompletionException) {
            if (e.getCause() instanceof NotAllowedException) {
              return AdmissionUtils.notAllowedExceptionToAdmissionResponse(
                  (NotAllowedException) e.getCause());
            } else {
              throw new IllegalStateException(e.getCause());
            }
            // todo check these exceptions how to handle correctly
          } else {
            throw new IllegalStateException(e);
          }
        });
  }

  private AdmissionResponse allowedAdmissionResponse() {
    AdmissionResponse admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(true);
    return admissionResponse;
  }
}
