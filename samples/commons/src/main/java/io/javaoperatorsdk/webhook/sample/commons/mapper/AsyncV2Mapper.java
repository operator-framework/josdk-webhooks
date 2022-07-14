package io.javaoperatorsdk.webhook.sample.commons.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

@TargetVersion("v2")
public class AsyncV2Mapper
    implements AsyncMapper<MultiVersionCustomResourceV2, MultiVersionCustomResourceV2> {

  private V2Mapper mapper = new V2Mapper();

  @Override
  public CompletionStage<MultiVersionCustomResourceV2> toHub(
      MultiVersionCustomResourceV2 resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<MultiVersionCustomResourceV2> fromHub(
      MultiVersionCustomResourceV2 multiVersionCustomResourceV2) {
    return CompletableFuture.completedStage(mapper.fromHub(multiVersionCustomResourceV2));
  }
}
