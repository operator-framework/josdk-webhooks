package io.javaoperatorsdk.admissioncontroller.mutation;

import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;

public interface Mutator<T> {

  T mutate(T resource, Operation operation) throws NotAllowedException;

}
