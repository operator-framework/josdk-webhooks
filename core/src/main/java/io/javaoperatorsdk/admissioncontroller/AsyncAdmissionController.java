package io.javaoperatorsdk.admissioncontroller;

import java.util.concurrent.CompletableFuture;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.mutation.AsyncDefaultRequestMutator;
import io.javaoperatorsdk.admissioncontroller.mutation.AsyncMutator;
import io.javaoperatorsdk.admissioncontroller.validation.AsyncDefaultRequestValidator;
import io.javaoperatorsdk.admissioncontroller.validation.Validator;

public class AsyncAdmissionController<T extends KubernetesResource> {

  private final AsyncRequestHandler requestHandler;

  public AsyncAdmissionController(AsyncMutator<T> mutator) {
    this(new AsyncDefaultRequestMutator<>(mutator));
  }

  public AsyncAdmissionController(Validator<T> mutator) {
    this(new AsyncDefaultRequestValidator<>(mutator));
  }

  public AsyncAdmissionController(AsyncRequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public CompletableFuture<AdmissionReview> handle(AdmissionReview admissionReview) {
    var response = requestHandler.handle(admissionReview.getRequest());
    return response.thenApply(r -> {
      AdmissionReview responseAdmissionReview = new AdmissionReview();
      responseAdmissionReview.setResponse(r);
      r.setUid(admissionReview.getRequest().getUid());
      return admissionReview;
    });
  }

}
