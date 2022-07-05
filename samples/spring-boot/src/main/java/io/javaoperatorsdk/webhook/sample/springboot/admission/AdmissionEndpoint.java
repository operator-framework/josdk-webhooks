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
public class AdmissionEndpoint {

  public static final String MUTATE_PATH = "mutate";
  public static final String VALIDATE_PATH = "validate";
  public static final String ERROR_MUTATE_PATH = "error-mutate";
  public static final String ERROR_VALIDATE_PATH = "error-validate";
  public static final String ASYNC_MUTATE_PATH = "async-mutate";
  public static final String ASYNC_VALIDATE_PATH = "async-validate";
  public static final String ERROR_ASYNC_MUTATE_PATH = "error-async-mutate";
  public static final String ERROR_ASYNC_VALIDATE_PATH = "error-async-validate";

  private final AdmissionController<Pod> mutatingController;
  private final AdmissionController<Pod> validatingController;
  private final AdmissionController<Pod> errorMutatingController;
  private final AdmissionController<Pod> errorValidatingController;
  private final AsyncAdmissionController<Pod> asyncMutatingController;
  private final AsyncAdmissionController<Pod> asyncValidatingController;
  private final AsyncAdmissionController<Pod> errorAsyncMutatingController;
  private final AsyncAdmissionController<Pod> errorAsyncValidatingController;

  @Autowired
  public AdmissionEndpoint(
      @Qualifier("mutatingController") AdmissionController<Pod> mutationController,
      @Qualifier("validatingController") AdmissionController<Pod> validatingController,
      @Qualifier("errorMutatingController") AdmissionController<Pod> errorMutatingController,
      @Qualifier("errorValidatingController") AdmissionController<Pod> errorValidatingController,
      @Qualifier("asyncMutatingController") AsyncAdmissionController<Pod> asyncMutatingController,
      @Qualifier("asyncValidatingController") AsyncAdmissionController<Pod> asyncValidatingController,
      @Qualifier("errorAsyncMutatingController") AsyncAdmissionController<Pod> errorAsyncMutatingController,
      @Qualifier("errorAsyncValidatingController") AsyncAdmissionController<Pod> errorAsyncValidatingController) {
    this.mutatingController = mutationController;
    this.validatingController = validatingController;
    this.errorMutatingController = errorMutatingController;
    this.errorValidatingController = errorValidatingController;
    this.asyncMutatingController = asyncMutatingController;
    this.asyncValidatingController = asyncValidatingController;
    this.errorAsyncMutatingController = errorAsyncMutatingController;
    this.errorAsyncValidatingController = errorAsyncValidatingController;
  }

  @PostMapping(MUTATE_PATH)
  @ResponseBody
  public AdmissionReview mutate(@RequestBody AdmissionReview admissionReview) {
    return mutatingController.handle(admissionReview);
  }

  @PostMapping(value = VALIDATE_PATH)
  @ResponseBody
  public AdmissionReview validate(@RequestBody AdmissionReview admissionReview) {
    return validatingController.handle(admissionReview);
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

  @PostMapping(ASYNC_MUTATE_PATH)
  @ResponseBody
  public Mono<AdmissionReview> asyncMutate(@RequestBody AdmissionReview admissionReview) {
    return Mono.fromCompletionStage(asyncMutatingController.handle(admissionReview));
  }

  @PostMapping(ASYNC_VALIDATE_PATH)
  @ResponseBody
  public Mono<AdmissionReview> asyncValidate(@RequestBody AdmissionReview admissionReview) {
    return Mono.fromCompletionStage(asyncValidatingController.handle(admissionReview));
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
