package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResourceV2;

@TargetVersion("v1")
public class V1Mapper implements Mapper<TestCustomResource, TestCustomResourceV2> {


  @Override
  public TestCustomResourceV2 toHub(TestCustomResource resource) {
    return null;
  }

  @Override
  public TestCustomResource fromHub(TestCustomResourceV2 testCustomResourceV2) {
    return null;
  }
}
