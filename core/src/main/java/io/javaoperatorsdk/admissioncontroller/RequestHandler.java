package io.javaoperatorsdk.admissioncontroller;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public interface RequestHandler {

  AdmissionResponse handle(AdmissionRequest admissionRequest);

}
