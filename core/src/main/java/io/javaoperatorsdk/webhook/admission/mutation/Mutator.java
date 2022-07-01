package io.javaoperatorsdk.webhook.admission.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;

public interface Mutator<T extends KubernetesResource> {

  T mutate(T resource, Operation operation) throws NotAllowedException;

}
