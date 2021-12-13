package io.javaoperatorsdk.admissioncontroller.sample;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;

public class SampleMutationHook {

  public AdmissionReview validate(AdmissionReview request) {

    return null;
  }

}
