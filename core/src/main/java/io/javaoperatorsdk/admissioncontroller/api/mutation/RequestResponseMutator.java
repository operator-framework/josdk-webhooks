package io.javaoperatorsdk.admissioncontroller.api.mutation;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;

public interface RequestResponseMutator {

    AdmissionResponse mutate(AdmissionRequest admissionRequest);

}
