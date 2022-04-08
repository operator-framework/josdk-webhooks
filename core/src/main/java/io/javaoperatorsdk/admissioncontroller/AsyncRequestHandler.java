package io.javaoperatorsdk.admissioncontroller;

import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public interface AsyncRequestHandler {

  CompletionStage<AdmissionResponse> handle(AdmissionRequest admissionRequest);

}
