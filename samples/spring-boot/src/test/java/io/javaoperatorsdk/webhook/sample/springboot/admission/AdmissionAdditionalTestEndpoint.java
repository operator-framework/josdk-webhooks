package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;

import reactor.core.publisher.Mono;

@RestController
public class AdmissionAdditionalTestEndpoint {

  public static final String ERROR_MUTATE_PATH = "error-mutate";
  public static final String ERROR_VALIDATE_PATH = "error-validate";
  public static final String ERROR_ASYNC_MUTATE_PATH = "error-async-mutate";
  public static final String ERROR_ASYNC_VALIDATE_PATH = "error-async-validate";

  private final AdmissionController<Ingress> errorMutatingController;
  private final AdmissionController<Ingress> errorValidatingController;
  private final AsyncAdmissionController<Ingress> errorAsyncMutatingController;
  private final AsyncAdmissionController<Ingress> errorAsyncValidatingController;

  @Autowired
  public AdmissionAdditionalTestEndpoint(
      @Qualifier("errorMutatingController") AdmissionController<Ingress> errorMutatingController,
      @Qualifier("errorValidatingController") AdmissionController<Ingress> errorValidatingController,
      @Qualifier("errorAsyncMutatingController") AsyncAdmissionController<Ingress> errorAsyncMutatingController,
      @Qualifier("errorAsyncValidatingController") AsyncAdmissionController<Ingress> errorAsyncValidatingController) {
    this.errorMutatingController = errorMutatingController;
    this.errorValidatingController = errorValidatingController;
    this.errorAsyncMutatingController = errorAsyncMutatingController;
    this.errorAsyncValidatingController = errorAsyncValidatingController;
  }

  @PostMapping(ERROR_MUTATE_PATH)
  @ResponseBody
  public AdmissionReview errorMutate(@RequestBody AdmissionReview admissionReview) {
    return errorMutatingController.handle(admissionReview);
  }

  @PostMapping(value = ERROR_VALIDATE_PATH)
  @ResponseBody
  public AdmissionReview errorValidate(@RequestBody AdmissionReview admissionReview) {
    return errorValidatingController.handle(admissionReview);
  }

  @PostMapping(ERROR_ASYNC_MUTATE_PATH)
  @ResponseBody
  public Mono<AdmissionReview> errorAsyncMutate(@RequestBody AdmissionReview admissionReview) {
    return Mono.fromCompletionStage(errorAsyncMutatingController.handle(admissionReview));
  }

  @PostMapping(ERROR_ASYNC_VALIDATE_PATH)
  @ResponseBody
  public Mono<AdmissionReview> errorAsyncValidate(@RequestBody AdmissionReview admissionReview) {
    return Mono.fromCompletionStage(errorAsyncValidatingController.handle(admissionReview));
  }

}
