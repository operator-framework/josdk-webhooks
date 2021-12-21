package io.javaoperatorsdk.admissioncontroller.validation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;

public interface Validator<T extends KubernetesResource> {

  void validate(T resource, Operation operation) throws NotAllowedException;

}
