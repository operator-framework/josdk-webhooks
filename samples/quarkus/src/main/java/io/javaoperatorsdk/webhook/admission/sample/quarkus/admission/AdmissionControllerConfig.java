package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.inject.Named;
import javax.inject.Singleton;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;

public class AdmissionControllerConfig {

  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";

  public static final String MUTATING_CONTROLLER = "mutatingController";
  public static final String VALIDATING_CONTROLLER = "validatingController";
  public static final String ASYNC_MUTATING_CONTROLLER = "asyncMutatingController";
  public static final String ASYNC_VALIDATING_CONTROLLER = "asyncValidatingController";

  @Singleton
  @Named(MUTATING_CONTROLLER)
  public AdmissionController<Pod> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null) {
        resource.getMetadata().setLabels(new HashMap<>());
      }

      resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
      return resource;
    });
  }

  @Singleton
  @Named(VALIDATING_CONTROLLER)
  public AdmissionController<Pod> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

  @Singleton
  @Named(ASYNC_MUTATING_CONTROLLER)
  public AsyncAdmissionController<Pod> asyncMutatingController() {
    return new AsyncAdmissionController<>(
        (AsyncMutator<Pod>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
          if (resource.getMetadata().getLabels() == null) {
            resource.getMetadata().setLabels(new HashMap<>());
          }

          resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
          return resource;
        }));
  }

  @Singleton
  @Named(ASYNC_VALIDATING_CONTROLLER)
  public AsyncAdmissionController<Pod> asyncValidatingController() {
    return new AsyncAdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }
}
