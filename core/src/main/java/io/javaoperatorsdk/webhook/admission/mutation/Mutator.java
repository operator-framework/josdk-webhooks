package io.javaoperatorsdk.webhook.admission.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;

/**
 * Any change made on the resource will be reflected in the response.
 *
 * @param <T> type of Kubernetes resources
 */
public interface Mutator<T extends KubernetesResource> {

  T mutate(T resource, Operation operation) throws NotAllowedException;

}
