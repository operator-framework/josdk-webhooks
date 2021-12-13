package io.javaoperatorsdk.admissioncontroller.api.clone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperCloner implements Cloner{

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <R> R clone(R object) {
        if (object == null) {
            return null;
        }
        try {
            return (R) OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(object), object.getClass());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
