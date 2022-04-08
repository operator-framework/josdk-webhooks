package io.javaoperatorsdk.admissioncontroller.sample.quarkus;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.inject.Singleton;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import io.javaoperatorsdk.admissioncontroller.AsyncAdmissionController;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.mutation.AsyncMutator;
import io.javaoperatorsdk.admissioncontroller.mutation.Mutator;
import io.javaoperatorsdk.admissioncontroller.validation.Validator;

@Dependent
public class AdmissionControllerConfig {

  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";

  public static final String MUTATING_CONTROLLER = "mutatingController";
  public static final String VALIDATING_CONTROLLER = "validatingController";
  public static final String ERROR_MUTATING_CONTROLLER = "errorMutatingController";
  public static final String ERROR_VALIDATING_CONTROLLER = "errorValidatingController";
  public static final String ASYNC_MUTATING_CONTROLLER = "asyncMutatingController";
  public static final String ASYNC_VALIDATING_CONTROLLER = "asyncValidatingController";
  public static final String ERROR_ASYNC_MUTATING_CONTROLLER = "errorAsyncMutatingController";
  public static final String ERROR_ASYNC_VALIDATING_CONTROLLER = "errorAsyncValidatingController";

  @Singleton
  @Named(MUTATING_CONTROLLER)
  public AdmissionController<Pod> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
      return resource;
    });
  }

  @Singleton
  @Named(VALIDATING_CONTROLLER)
  public AdmissionController<Pod> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

  @Singleton
  @Named(ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Pod> asyncMutatingController() {
    return new AsyncAdmissionController<>(
        (AsyncMutator<Pod>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
          resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
          return resource;
        }));
  }

  @Singleton
  @Named(ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Pod> asyncValidatingController() {
    return new AsyncAdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }


  @Singleton
  @Named(ERROR_MUTATING_CONTROLLER)
  public AdmissionController<Pod> errorMutatingController() {
    return new AdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException("Some error happened");
    });
  }

  @Singleton
  @Named(ERROR_VALIDATING_CONTROLLER)
  public AdmissionController<Pod> errorValidatingController() {
    return new AdmissionController<>((Mutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException("Some error happened");
    });
  }

  @Singleton
  @Named(ERROR_ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Pod> errorAsyncMutatingController() {
    return new AsyncAdmissionController<>((AsyncMutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException("Some error happened");
    });
  }

  @Singleton
  @Named(ERROR_ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Pod> errorAsyncValidatingController() {
    return new AsyncAdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException("Some error happened");
    });
  }

}
