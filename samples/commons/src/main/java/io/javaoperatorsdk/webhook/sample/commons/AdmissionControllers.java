package io.javaoperatorsdk.webhook.sample.commons;

import java.util.HashMap;

import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

public class AdmissionControllers {

  public static final String ERROR_MESSAGE = "Some error happened";
  public static final String VALIDATION_TARGET_LABEL = "app.kubernetes.io/name";
  public static final String MUTATION_TARGET_LABEL = "app.kubernetes.io/id";

  // adds a label to the target resource
  public static AdmissionController<Ingress> mutatingController() {
    return new AdmissionController<>(new IngressMutator());
  }

  // validates if a resource contains the target label
  public static AdmissionController<Ingress> validatingController() {
    return new AdmissionController<>(new IngressValidator());
  }

  public static AsyncAdmissionController<Ingress> asyncMutatingController() {
    return new AsyncAdmissionController<>(new IngressMutator());
  }

  public static AsyncAdmissionController<Ingress> asyncValidatingController() {
    return new AsyncAdmissionController<>(new IngressValidator());
  }

  public static AdmissionController<Ingress> errorMutatingController() {
    return new AdmissionController<>((Validator<Ingress>) (resource, oldResource, operation) -> {
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
    return new AsyncAdmissionController<>(
        (Validator<Ingress>) (resource, oldResource, operation) -> {
          throw new IllegalStateException(ERROR_MESSAGE);
        });
  }

  private static class IngressMutator implements Mutator<Ingress> {
    @Override
    public Ingress mutate(Ingress resource, Operation operation) throws NotAllowedException {
      if (resource.getMetadata().getLabels() == null) {
        resource.getMetadata().setLabels(new HashMap<>());
      }
      resource.getMetadata().getLabels().putIfAbsent(MUTATION_TARGET_LABEL, "mutation-test");
      return resource;
    }
  }

  private static class IngressValidator implements Validator<Ingress> {
    @Override
    public void validate(Ingress resource, Ingress oldResource, Operation operation)
        throws NotAllowedException {
      if (resource.getMetadata().getLabels() == null
          || resource.getMetadata().getLabels().get(VALIDATION_TARGET_LABEL) == null) {
        throw new NotAllowedException("Missing label: " + VALIDATION_TARGET_LABEL);
      }
      if (!resource.getSpec().getIngressClassName()
          .equals(oldResource.getSpec().getIngressClassName())) {
        throw new NotAllowedException("IngressClassName cannot be changed");
      }
    }
  }
}
