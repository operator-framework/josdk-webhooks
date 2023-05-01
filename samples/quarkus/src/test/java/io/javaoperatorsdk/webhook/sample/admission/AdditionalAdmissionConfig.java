package io.javaoperatorsdk.webhook.sample.admission;

import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

public class AdditionalAdmissionConfig {

  public static final String ERROR_MUTATING_CONTROLLER = "errorMutatingController";
  public static final String ERROR_VALIDATING_CONTROLLER = "errorValidatingController";
  public static final String ERROR_ASYNC_MUTATING_CONTROLLER = "errorAsyncMutatingController";
  public static final String ERROR_ASYNC_VALIDATING_CONTROLLER = "errorAsyncValidatingController";

  @Singleton
  @Named(ERROR_MUTATING_CONTROLLER)
  public AdmissionController<Ingress> errorMutatingController() {
    return AdmissionControllers.errorMutatingController();
  }

  @Singleton
  @Named(ERROR_VALIDATING_CONTROLLER)
  public AdmissionController<Ingress> errorValidatingController() {
    return AdmissionControllers.errorValidatingController();
  }

  @Singleton
  @Named(ERROR_ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Ingress> errorAsyncMutatingController() {
    return AdmissionControllers.errorAsyncMutatingController();
  }

  @Singleton
  @Named(ERROR_ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Ingress> errorAsyncValidatingController() {
    return AdmissionControllers.errorAsyncValidatingController();
  }
}
