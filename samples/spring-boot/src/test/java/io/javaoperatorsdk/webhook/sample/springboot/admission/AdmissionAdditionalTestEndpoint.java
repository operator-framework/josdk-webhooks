package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;

import reactor.core.publisher.Mono;

@RestController
public class AdmissionAdditionalTestEndpoint {

  public static final String ERROR_MUTATE_PATH = "error-mutate";
  public static final String ERROR_VALIDATE_PATH = "error-validate";
  public static final String ERROR_ASYNC_MUTATE_PATH = "error-async-mutate";
  public static final String ERROR_ASYNC_VALIDATE_PATH = "error-async-validate";

  private final AdmissionController<Pod> errorMutatingController;
  private final AdmissionController<Pod> errorValidatingController;
  private final AsyncAdmissionController<Pod> errorAsyncMutatingController;
  private final AsyncAdmissionController<Pod> errorAsyncValidatingController;

  @Autowired
  public AdmissionAdditionalTestEndpoint(
      @Qualifier("errorMutatingController") AdmissionController<Pod> errorMutatingController,
      @Qualifier("errorValidatingController") AdmissionController<Pod> errorValidatingController,
      @Qualifier("errorAsyncMutatingController") AsyncAdmissionController<Pod> errorAsyncMutatingController,
      @Qualifier("errorAsyncValidatingController") AsyncAdmissionController<Pod> errorAsyncValidatingController) {
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
