package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import javax.inject.Named;
import javax.inject.Singleton;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

public class AdditionalAdmissionConfig {

  public static final String ERROR_MUTATING_CONTROLLER = "errorMutatingController";
  public static final String ERROR_VALIDATING_CONTROLLER = "errorValidatingController";
  public static final String ERROR_ASYNC_MUTATING_CONTROLLER = "errorAsyncMutatingController";
  public static final String ERROR_ASYNC_VALIDATING_CONTROLLER = "errorAsyncValidatingController";

  @Singleton
  @Named(ERROR_MUTATING_CONTROLLER)
  public AdmissionController<Pod> errorMutatingController() {
    return AdmissionControllers.errorMutatingController();
  }

  @Singleton
  @Named(ERROR_VALIDATING_CONTROLLER)
  public AdmissionController<Pod> errorValidatingController() {
    return AdmissionControllers.errorValidatingController();
  }

  @Singleton
  @Named(ERROR_ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Pod> errorAsyncMutatingController() {
    return AdmissionControllers.errorAsyncMutatingController();
  }

  @Singleton
  @Named(ERROR_ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Pod> errorAsyncValidatingController() {
    return AdmissionControllers.errorAsyncValidatingController();
  }
}
