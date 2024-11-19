package io.javaoperatorsdk.webhook.clone;

public interface Cloner<R> {

  /**
   * Returns a deep copy of the given object if not {@code null} or {@code null} otherwise.
   *
   * @param object the object to be cloned
   * @return a deep copy of the given object if it isn't {@code null}, {@code null} otherwise
   */
  R clone(R object);
}
