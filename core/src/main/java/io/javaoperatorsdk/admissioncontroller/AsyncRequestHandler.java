package io.javaoperatorsdk.admissioncontroller;

import java.util.concurrent.CompletableFuture;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public interface AsyncRequestHandler {

  CompletableFuture<AdmissionResponse> handle(AdmissionRequest admissionRequest);

}
