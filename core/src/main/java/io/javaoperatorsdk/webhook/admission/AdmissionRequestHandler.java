package io.javaoperatorsdk.webhook.admission;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public interface AdmissionRequestHandler {

  AdmissionResponse handle(AdmissionRequest admissionRequest);

}
