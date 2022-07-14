package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

@TargetVersion("v2")
public class V2Mapper
    implements Mapper<MultiVersionCustomResourceV2, MultiVersionCustomResourceV2> {

  @Override
  public MultiVersionCustomResourceV2 toHub(MultiVersionCustomResourceV2 resource) {
    return resource;
  }

  @Override
  public MultiVersionCustomResourceV2 fromHub(
      MultiVersionCustomResourceV2 multiVersionCustomResourceV2) {
    return multiVersionCustomResourceV2;
  }
}
