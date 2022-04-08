package io.javaoperatorsdk.admissioncontroller.validation;

import java.util.concurrent.CompletableFuture;

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
  public CompletableFuture<AdmissionResponse> handle(AdmissionRequest admissionRequest) {
    Operation operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);
    CompletableFuture<AdmissionResponse> admissionResponse;
    try {
      var asyncValidate =
          CompletableFuture.runAsync(() -> validator.validate(originalResource, operation));
      admissionResponse = asyncValidate.thenApply(v -> allowedAdmissionResponse());
    } catch (NotAllowedException e) {
      admissionResponse = CompletableFuture
          .supplyAsync(() -> AdmissionUtils.notAllowedExceptionToAdmissionResponse(e));
    }
    return admissionResponse;
  }

  private AdmissionResponse allowedAdmissionResponse() {
    AdmissionResponse admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(true);
    return admissionResponse;
  }
}
