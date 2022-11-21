package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import javax.inject.Named;
import javax.inject.Singleton;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

public class AdditionalAdmissionConfig {

  public static final String ERROR_MUTATING_CONTROLLER = "errorMutatingController";
  public static final String ERROR_VALIDATING_CONTROLLER = "errorValidatingController";
  public static final String ERROR_ASYNC_MUTATING_CONTROLLER = "errorAsyncMutatingController";
  public static final String ERROR_ASYNC_VALIDATING_CONTROLLER = "errorAsyncValidatingController";
  public static final String ERROR_MESSAGE = "Some error happened";

  @Singleton
  @Named(ERROR_MUTATING_CONTROLLER)
  public AdmissionController<Pod> errorMutatingController() {
    return new AdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  @Singleton
  @Named(ERROR_VALIDATING_CONTROLLER)
  public AdmissionController<Pod> errorValidatingController() {
    return new AdmissionController<>((Mutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  @Singleton
  @Named(ERROR_ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Pod> errorAsyncMutatingController() {
    return new AsyncAdmissionController<>((AsyncMutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  @Singleton
  @Named(ERROR_ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Pod> errorAsyncValidatingController() {
    return new AsyncAdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }
}
