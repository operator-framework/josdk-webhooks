package io.javaoperatorsdk.admissioncontroller.admission.validation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.admissioncontroller.admission.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.admission.Operation;

public interface Validator<T extends KubernetesResource> {

  void validate(T resource, Operation operation) throws NotAllowedException;

}
