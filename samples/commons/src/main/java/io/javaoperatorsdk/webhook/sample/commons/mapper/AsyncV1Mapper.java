package io.javaoperatorsdk.webhook.sample.commons.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResourceV2;

@TargetVersion("v1")
public class AsyncV1Mapper implements AsyncMapper<TestCustomResource, TestCustomResourceV2> {

  private V1Mapper mapper = new V1Mapper();

  @Override
  public CompletionStage<TestCustomResourceV2> toHub(TestCustomResource resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<TestCustomResource> fromHub(TestCustomResourceV2 testCustomResourceV2) {
    return CompletableFuture.completedStage(mapper.fromHub(testCustomResourceV2));
  }
}
