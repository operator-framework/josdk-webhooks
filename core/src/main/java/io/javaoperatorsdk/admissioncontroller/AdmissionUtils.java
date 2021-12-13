package io.javaoperatorsdk.admissioncontroller;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public class AdmissionUtils {

  public static AdmissionResponse notAllowedExceptionToAdmissionResponse(
      NotAllowedException notAllowedException) {
    AdmissionResponse admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(false);
    Status status = new Status();
    status.setCode(notAllowedException.getCode());
    status.setMessage(notAllowedException.getMessage());
    admissionResponse.setStatus(status);
    return admissionResponse;
  }

  public static KubernetesResource getTargetResource(AdmissionRequest admissionRequest,
      Operation operation) {
    return operation == Operation.DELETE ? admissionRequest.getOldObject()
        : admissionRequest.getObject();
  }
}
