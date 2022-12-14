package io.javaoperatorsdk.webhook.sample.commons;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

public class AdmissionControllers {

  public static final String ERROR_MESSAGE = "Some error happened";
  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";
  public static final String ANOTHER_LABEL_TO_ADD = "app.kubernetes.io/id";

  public static AdmissionController<Ingress> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null) {
        resource.getMetadata().setLabels(new HashMap<>());
      }
      resource.getMetadata().getLabels().putIfAbsent(ANOTHER_LABEL_TO_ADD, "mutation-test");
      return resource;
    });
  }

  public static AdmissionController<Ingress> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

  public static AsyncAdmissionController<Ingress> asyncMutatingController() {
    return new AsyncAdmissionController<>(
        (AsyncMutator<Ingress>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
          if (resource.getMetadata().getLabels() == null) {
            resource.getMetadata().setLabels(new HashMap<>());
          }
          resource.getMetadata().getLabels().putIfAbsent(ANOTHER_LABEL_TO_ADD, "mutation-test");
          return resource;
        }));
  }

  public static AsyncAdmissionController<Ingress> asyncValidatingController() {
    return new AsyncAdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }


  public static AdmissionController<Ingress> errorMutatingController() {
    return new AdmissionController<>((Validator<Ingress>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  public static AdmissionController<Ingress> errorValidatingController() {
    return new AdmissionController<>((Mutator<Ingress>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  public static AsyncAdmissionController<Ingress> errorAsyncMutatingController() {
    return new AsyncAdmissionController<>((AsyncMutator<Ingress>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  public static AsyncAdmissionController<Ingress> errorAsyncValidatingController() {
    return new AsyncAdmissionController<>((Validator<Ingress>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }
}
