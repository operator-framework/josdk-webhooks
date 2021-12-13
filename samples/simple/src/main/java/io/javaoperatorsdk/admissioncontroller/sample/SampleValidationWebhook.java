package io.javaoperatorsdk.admissioncontroller.sample;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;

public class SampleValidationWebhook {




    public AdmissionReview validate(AdmissionReview request) {


        return null;
    }

    private AdmissionResponse validate(AdmissionRequest admissionRequest) {
        return null;
    }


}
