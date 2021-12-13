package io.javaoperatorsdk.admissioncontroller.sample;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;

public class SampleMutationHook {

    public AdmissionReview validate(AdmissionReview request) {

        return null;
    }

}
