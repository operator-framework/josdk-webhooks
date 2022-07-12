package io.javaoperatorsdk.webhook.sample.commons.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

@TargetVersion("v1")
public class AsyncV1Mapper
    implements AsyncMapper<MultiVersionCustomResource, MultiVersionCustomResourceV2> {

  private V1Mapper mapper = new V1Mapper();

  @Override
  public CompletionStage<MultiVersionCustomResourceV2> toHub(MultiVersionCustomResource resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<MultiVersionCustomResource> fromHub(
      MultiVersionCustomResourceV2 testCustomResourceV2) {
    return CompletableFuture.completedStage(mapper.fromHub(testCustomResourceV2));
  }
}
