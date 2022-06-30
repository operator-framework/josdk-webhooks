package io.javaoperatorsdk.admissioncontroller.admission.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.admissioncontroller.admission.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.admission.Operation;

public interface Mutator<T extends KubernetesResource> {

  T mutate(T resource, Operation operation) throws NotAllowedException;

}
