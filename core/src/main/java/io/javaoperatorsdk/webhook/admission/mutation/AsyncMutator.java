package io.javaoperatorsdk.webhook.admission.mutation;

import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;

public interface AsyncMutator<T extends KubernetesResource> {

  CompletionStage<T> mutate(T resource, Operation operation) throws NotAllowedException;
}
