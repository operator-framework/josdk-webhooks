package io.javaoperatorsdk.admissioncontroller.clone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperCloner<T> implements Cloner<T> {

  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public T clone(T object) {
    if (object == null) {
      return null;
    }
    try {
      return (T) OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(object),
          object.getClass());
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }
}
