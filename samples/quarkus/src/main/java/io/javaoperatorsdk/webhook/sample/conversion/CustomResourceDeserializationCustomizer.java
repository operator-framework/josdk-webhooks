package io.javaoperatorsdk.webhook.sample.conversion;

import javax.inject.Singleton;

import io.javaoperatorsdk.webhook.conversion.Utils;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;
import io.quarkus.jackson.ObjectMapperCustomizer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * For quarkus for now the custom kinds needs to be registered explicitly
 */
@Singleton
public class CustomResourceDeserializationCustomizer implements ObjectMapperCustomizer {

  @Override
  public void customize(ObjectMapper objectMapper) {
    Utils.registerCustomKind(MultiVersionCustomResource.class);
    Utils.registerCustomKind(MultiVersionCustomResourceV2.class);
  }
}
