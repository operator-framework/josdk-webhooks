package io.javaoperatorsdk.admissioncontroller.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;

public interface Mutator<T extends KubernetesResource> {

  T mutate(T resource, Operation operation) throws NotAllowedException;

}
