package io.javaoperatorsdk.admissioncontroller.validation;

import io.javaoperatorsdk.admissioncontroller.Operation;

public interface Validator<T> {

  void validate(T resource, Operation operation);

}
