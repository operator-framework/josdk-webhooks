package io.javaoperatorsdk.webhook.conversion.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;

@TargetVersion("v3")
public class CustomResourceV3Mapper implements Mapper<CustomResourceV3, CustomResourceV3> {

  @Override
  public CustomResourceV3 toHub(CustomResourceV3 resource) {
    return resource;
  }

  @Override
  public CustomResourceV3 fromHub(CustomResourceV3 hub) {
    return hub;
  }
}
