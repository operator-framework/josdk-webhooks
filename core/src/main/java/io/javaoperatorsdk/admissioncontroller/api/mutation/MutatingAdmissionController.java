package io.javaoperatorsdk.admissioncontroller.api.mutation;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;

public class MutatingAdmissionController<T> {

    private final RequestResponseMutator requestResponseMutator;

    public MutatingAdmissionController(Mutator<T> mutator) {
        this(new DefaultRequestResponseMutator<>(mutator));
    }

    public MutatingAdmissionController(RequestResponseMutator requestResponseMutator) {
        this.requestResponseMutator = requestResponseMutator;
    }

    public AdmissionReview handle(AdmissionReview admissionReview) {
        var response = requestResponseMutator.mutate(admissionReview.getRequest());
        AdmissionReview responseAdmissionReview = new AdmissionReview();
        responseAdmissionReview.setResponse(response);
        response.setUid(admissionReview.getRequest().getUid());
        return responseAdmissionReview;
    }
}
