package io.javaoperatorsdk.admissioncontroller.admission;

import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.admission.mutation.AsyncDefaultAdmissionRequestMutator;
import io.javaoperatorsdk.admissioncontroller.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.admissioncontroller.admission.validation.AsyncDefaultAdmissionRequestValidator;
import io.javaoperatorsdk.admissioncontroller.admission.validation.Validator;

public class AsyncAdmissionController<T extends KubernetesResource> {

  private final AsyncAdmissionRequestHandler requestHandler;

  public AsyncAdmissionController(AsyncMutator<T> mutator) {
    this(new AsyncDefaultAdmissionRequestMutator<>(mutator));
  }

  public AsyncAdmissionController(Validator<T> validator) {
    this(new AsyncDefaultAdmissionRequestValidator<>(validator));
  }

  public AsyncAdmissionController(AsyncAdmissionRequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public CompletionStage<AdmissionReview> handle(AdmissionReview admissionReview) {
    return requestHandler.handle(admissionReview.getRequest())
        .thenApply(r -> {
          AdmissionReview responseAdmissionReview = new AdmissionReview();
          responseAdmissionReview.setResponse(r);
          r.setUid(admissionReview.getRequest().getUid());
          return responseAdmissionReview;
        });
  }

}
