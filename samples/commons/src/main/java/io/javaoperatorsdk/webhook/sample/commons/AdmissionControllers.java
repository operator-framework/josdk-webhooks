package io.javaoperatorsdk.webhook.sample.commons;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

public class AdmissionControllers {

  public static final String ERROR_MESSAGE = "Some error happened";
  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";

  public static AdmissionController<Pod> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null) {
        resource.getMetadata().setLabels(new HashMap<>());
      }
      resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
      return resource;
    });
  }

  public static AdmissionController<Pod> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

  public static AsyncAdmissionController<Pod> asyncMutatingController() {
    return new AsyncAdmissionController<>(
        (AsyncMutator<Pod>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
          if (resource.getMetadata().getLabels() == null) {
            resource.getMetadata().setLabels(new HashMap<>());
          }
          resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
          return resource;
        }));
  }

  public static AsyncAdmissionController<Pod> asyncValidatingController() {
    return new AsyncAdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }


  public static AdmissionController<Pod> errorMutatingController() {
    return new AdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  public static AdmissionController<Pod> errorValidatingController() {
    return new AdmissionController<>((Mutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  public static AsyncAdmissionController<Pod> errorAsyncMutatingController() {
    return new AsyncAdmissionController<>((AsyncMutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  public static AsyncAdmissionController<Pod> errorAsyncValidatingController() {
    return new AsyncAdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }
}
