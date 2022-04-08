package io.javaoperatorsdk.admissioncontroller.mutation;

import java.util.concurrent.CompletableFuture;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;

public interface AsyncMutator<T extends KubernetesResource> {

  CompletableFuture<T> mutate(T resource, Operation operation) throws NotAllowedException;

}
