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
  public static final String ASYNC_MUTATE_PATH = "async-mutate";
  public static final String ASYNC_VALIDATE_PATH = "async-validate";

  private final AdmissionController<Pod> mutatingController;
  private final AdmissionController<Pod> validatingController;
  private final AsyncAdmissionController<Pod> asyncMutatingController;
  private final AsyncAdmissionController<Pod> asyncValidatingController;

  @Autowired
  public AdmissionEndpoint(
      @Qualifier("mutatingController") AdmissionController<Pod> mutationController,
      @Qualifier("validatingController") AdmissionController<Pod> validatingController,
      @Qualifier("asyncMutatingController") AsyncAdmissionController<Pod> asyncMutatingController,
      @Qualifier("asyncValidatingController") AsyncAdmissionController<Pod> asyncValidatingController) {
    this.mutatingController = mutationController;
    this.validatingController = validatingController;
    this.asyncMutatingController = asyncMutatingController;
    this.asyncValidatingController = asyncValidatingController;
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

}
