package io.javaoperatorsdk.admissioncontroller.admission.validation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.admission.AdmissionUtils;
import io.javaoperatorsdk.admissioncontroller.admission.AsyncAdmissionRequestHandler;
import io.javaoperatorsdk.admissioncontroller.admission.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.admission.Operation;

import static io.javaoperatorsdk.admissioncontroller.admission.AdmissionUtils.getTargetResource;

public class AsyncDefaultAdmissionRequestValidator<T extends KubernetesResource>
    implements AsyncAdmissionRequestHandler {

  private final Validator<T> validator;

  public AsyncDefaultAdmissionRequestValidator(Validator<T> validator) {
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
