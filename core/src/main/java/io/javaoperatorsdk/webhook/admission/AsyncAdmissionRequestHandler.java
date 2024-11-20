package io.javaoperatorsdk.webhook.admission;

import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public interface AsyncAdmissionRequestHandler {

  CompletionStage<AdmissionResponse> handle(AdmissionRequest admissionRequest);
}
