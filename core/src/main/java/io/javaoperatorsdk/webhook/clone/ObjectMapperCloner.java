package io.javaoperatorsdk.webhook.clone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperCloner<T> implements Cloner<T> {

  private final ObjectMapper object_mapper = new ObjectMapper();

  @Override
  public T clone(T object) {
    if (object == null) {
      return null;
    }
    try {
      return (T) object_mapper.readValue(object_mapper.writeValueAsString(object),
          object.getClass());
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }
}
