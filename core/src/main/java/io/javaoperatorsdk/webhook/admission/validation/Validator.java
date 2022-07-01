package io.javaoperatorsdk.webhook.admission.validation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;

public interface Validator<T extends KubernetesResource> {

  void validate(T resource, Operation operation) throws NotAllowedException;

}
