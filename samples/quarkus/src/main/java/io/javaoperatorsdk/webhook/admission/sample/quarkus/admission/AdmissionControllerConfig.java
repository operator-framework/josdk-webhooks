package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

import javax.inject.Named;
import javax.inject.Singleton;

public class AdmissionControllerConfig {

  public static final String MUTATING_CONTROLLER = "mutatingController";
  public static final String VALIDATING_CONTROLLER = "validatingController";
  public static final String ASYNC_MUTATING_CONTROLLER = "asyncMutatingController";
  public static final String ASYNC_VALIDATING_CONTROLLER = "asyncValidatingController";

  @Singleton
  @Named(MUTATING_CONTROLLER)
  public AdmissionController<Pod> mutatingController() {
    return AdmissionControllers.mutatingController();
  }

  @Singleton
  @Named(VALIDATING_CONTROLLER)
  public AdmissionController<Pod> validatingController() {
    return AdmissionControllers.validatingController();
  }

  @Singleton
  @Named(ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Pod> asyncMutatingController() {
    return AdmissionControllers.asyncMutatingController();
  }

  @Singleton
  @Named(ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Pod> asyncValidatingController() {
    return AdmissionControllers.asyncValidatingController();
  }
}
