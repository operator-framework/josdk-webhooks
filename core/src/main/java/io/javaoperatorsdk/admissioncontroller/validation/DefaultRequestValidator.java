package io.javaoperatorsdk.admissioncontroller.validation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.AdmissionUtils;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;
import io.javaoperatorsdk.admissioncontroller.RequestHandler;

import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.getTargetResource;

public class DefaultRequestValidator<T extends KubernetesResource> implements RequestHandler {

  private final Validator<T> validator;

  public DefaultRequestValidator(Validator<T> validator) {
    this.validator = validator;
  }

  @Override
  public AdmissionResponse handle(AdmissionRequest admissionRequest) {
    Operation operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);
    AdmissionResponse admissionResponse;
    try {
      validator.validate(originalResource, operation);
      admissionResponse = allowedAdmissionResponse();
    } catch (NotAllowedException e) {
      admissionResponse = AdmissionUtils.notAllowedExceptionToAdmissionResponse(e);
    }
    return admissionResponse;
  }

  private AdmissionResponse allowedAdmissionResponse() {
    AdmissionResponse admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(true);
    return admissionResponse;
  }
}
