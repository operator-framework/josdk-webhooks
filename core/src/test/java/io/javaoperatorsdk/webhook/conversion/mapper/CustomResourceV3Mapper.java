package io.javaoperatorsdk.webhook.conversion.mapper;

import io.javaoperatorsdk.webhook.conversion.AbstractMapper;
import io.javaoperatorsdk.webhook.conversion.crd.*;

public class CustomResourceV3Mapper extends AbstractMapper<CustomResourceV3, CustomResourceV3> {

  public CustomResourceV3Mapper() {
    super("v3");
  }

  @Override
  public CustomResourceV3 toHub(CustomResourceV3 resource) {
    return resource;
  }

  @Override
  public CustomResourceV3 fromHub(CustomResourceV3 hub) {
    return hub;
  }
}
