package io.javaoperatorsdk.webhook.admission.validation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.webhook.admission.AdmissionRequestHandler;
import io.javaoperatorsdk.webhook.admission.AdmissionUtils;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;

import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.getTargetResource;

public class DefaultAdmissionRequestValidator<T extends KubernetesResource>
    implements AdmissionRequestHandler {

  private final Validator<T> validator;

  public DefaultAdmissionRequestValidator(Validator<T> validator) {
    this.validator = validator;
  }

  @Override
  @SuppressWarnings("unchecked")
  public AdmissionResponse handle(AdmissionRequest admissionRequest) {
    var operation = Operation.valueOf(admissionRequest.getOperation());
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
