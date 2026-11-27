package io.javaoperatorsdk.webhook.clone;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class ObjectMapperCloner<T> implements Cloner<T> {

  // Jackson 2 defaults (as used by the fabric8 client), e.g. getter-only collections are populated.
  private final ObjectMapper objectMapper = JsonMapper.builderWithJackson2Defaults().build();

  @Override
  @SuppressWarnings("unchecked")
  public T clone(T object) {
    if (object == null) {
      return null;
    }
    return (T) objectMapper.readValue(objectMapper.writeValueAsString(object), object.getClass());
  }
}
