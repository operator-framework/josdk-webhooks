package io.javaoperatorsdk.webhook.clone;

import tools.jackson.databind.ObjectMapper;

public class ObjectMapperCloner<T> implements Cloner<T> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @SuppressWarnings("unchecked")
  public T clone(T object) {
    if (object == null) {
      return null;
    }
    return (T) objectMapper.readValue(objectMapper.writeValueAsString(object), object.getClass());
  }
}
