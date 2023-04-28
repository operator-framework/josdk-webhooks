package io.javaoperatorsdk.webhook.sample.admission;

import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

public class AdmissionControllerConfig {

  public static final String MUTATING_CONTROLLER = "mutatingController";
  public static final String VALIDATING_CONTROLLER = "validatingController";
  public static final String ASYNC_MUTATING_CONTROLLER = "asyncMutatingController";
  public static final String ASYNC_VALIDATING_CONTROLLER = "asyncValidatingController";

  @Singleton
  @Named(MUTATING_CONTROLLER)
  public AdmissionController<Ingress> mutatingController() {
    return AdmissionControllers.mutatingController();
  }

  @Singleton
  @Named(VALIDATING_CONTROLLER)
  public AdmissionController<Ingress> validatingController() {
    return AdmissionControllers.validatingController();
  }

  @Singleton
  @Named(ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Ingress> asyncMutatingController() {
    return AdmissionControllers.asyncMutatingController();
  }

  @Singleton
  @Named(ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Ingress> asyncValidatingController() {
    return AdmissionControllers.asyncValidatingController();
  }
}
