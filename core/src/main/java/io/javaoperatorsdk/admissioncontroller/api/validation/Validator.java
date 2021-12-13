package io.javaoperatorsdk.admissioncontroller.api.validation;

public interface Validator<T> {

    default void validateCreate(T resource) {}

    default void validateUpdate(T oldResource, T newResource) {}

    default void validateDelete(T resource) {}

}
