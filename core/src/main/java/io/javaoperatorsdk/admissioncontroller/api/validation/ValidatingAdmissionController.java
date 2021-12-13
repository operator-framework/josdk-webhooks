package io.javaoperatorsdk.admissioncontroller.api.validation;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;

public class ValidatingAdmissionController {

    public AdmissionReview handle(AdmissionReview admissionReview) {
        return null;
    }

}
