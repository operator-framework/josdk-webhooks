package io.javaoperatorsdk.admissioncontroller.api.mutation;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.api.clone.Cloner;
import io.javaoperatorsdk.admissioncontroller.api.clone.ObjectMapperCloner;

public class DefaultRequestResponseMutator<T> implements RequestResponseMutator {

    private final Mutator<T> mutator;
    private final Cloner cloner;

    public DefaultRequestResponseMutator(Mutator<T> mutator) {
        this(mutator, new ObjectMapperCloner());
    }

    public DefaultRequestResponseMutator(Mutator<T> mutator, Cloner cloner) {
        this.mutator = mutator;
        this.cloner = cloner;
    }

    @Override
    public AdmissionResponse mutate(AdmissionRequest admissionRequest) {
        admissionRequest.getObject();


        return null;
    }

}
