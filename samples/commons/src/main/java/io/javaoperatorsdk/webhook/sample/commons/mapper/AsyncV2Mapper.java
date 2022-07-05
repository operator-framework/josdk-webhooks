package io.javaoperatorsdk.webhook.sample.commons.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResourceV2;

@TargetVersion("v2")
public class AsyncV2Mapper implements AsyncMapper<TestCustomResourceV2, TestCustomResourceV2> {

  private V2Mapper mapper = new V2Mapper();

  @Override
  public CompletionStage<TestCustomResourceV2> toHub(TestCustomResourceV2 resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<TestCustomResourceV2> fromHub(TestCustomResourceV2 testCustomResourceV2) {
    return CompletableFuture.completedStage(mapper.fromHub(testCustomResourceV2));
  }
}
