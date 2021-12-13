package io.javaoperatorsdk.admissioncontroller.api.mutation;

public interface Mutator<T> {

    T mutate(T resource);

}
