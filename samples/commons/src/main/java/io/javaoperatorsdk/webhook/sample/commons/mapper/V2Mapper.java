package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResourceV2;

@TargetVersion("v2")
public class V2Mapper implements Mapper<TestCustomResourceV2, TestCustomResourceV2> {

  @Override
  public TestCustomResourceV2 toHub(TestCustomResourceV2 resource) {
    return resource;
  }

  @Override
  public TestCustomResourceV2 fromHub(TestCustomResourceV2 testCustomResourceV2) {
    return testCustomResourceV2;
  }
}
