package io.javaoperatorsdk.webhook.sample.commons.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResource;

@TargetVersion("v1")
public class AsyncV1Mapper
    implements AsyncMapper<MultiVersionCustomResource, MultiVersionHub> {

  private V1Mapper mapper = new V1Mapper();

  @Override
  public CompletionStage<MultiVersionHub> toHub(MultiVersionCustomResource resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<MultiVersionCustomResource> fromHub(
      MultiVersionHub hub) {
    return CompletableFuture.completedStage(mapper.fromHub(hub));
  }
}
