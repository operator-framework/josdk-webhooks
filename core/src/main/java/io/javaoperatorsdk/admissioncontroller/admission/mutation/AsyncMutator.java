package io.javaoperatorsdk.admissioncontroller.admission.mutation;

import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.admissioncontroller.admission.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.admission.Operation;

public interface AsyncMutator<T extends KubernetesResource> {

  CompletionStage<T> mutate(T resource, Operation operation) throws NotAllowedException;

}
