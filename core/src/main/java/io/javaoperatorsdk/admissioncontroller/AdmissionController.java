package io.javaoperatorsdk.admissioncontroller;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.mutation.DefaultRequestMutator;
import io.javaoperatorsdk.admissioncontroller.mutation.Mutator;
import io.javaoperatorsdk.admissioncontroller.validation.DefaultRequestValidator;
import io.javaoperatorsdk.admissioncontroller.validation.Validator;

public class AdmissionController<T extends KubernetesResource> {

  private final RequestHandler requestHandler;

  public AdmissionController(Mutator<T> mutator) {
    this(new DefaultRequestMutator<>(mutator));
  }

  public AdmissionController(Validator<T> mutator) {
    this(new DefaultRequestValidator<>(mutator));
  }

  public AdmissionController(RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public AdmissionReview handle(AdmissionReview admissionReview) {
    var response = requestHandler.handle(admissionReview.getRequest());
    AdmissionReview responseAdmissionReview = new AdmissionReview();
    responseAdmissionReview.setResponse(response);
    response.setUid(admissionReview.getRequest().getUid());
    return responseAdmissionReview;
  }
}
