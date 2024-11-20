package io.javaoperatorsdk.webhook.sample.commons.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

@TargetVersion("v2")
public class AsyncV2Mapper implements AsyncMapper<MultiVersionCustomResourceV2, MultiVersionHub> {

  private final V2Mapper mapper = new V2Mapper();

  @Override
  public CompletionStage<MultiVersionHub> toHub(MultiVersionCustomResourceV2 resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<MultiVersionCustomResourceV2> fromHub(MultiVersionHub multiVersionHub) {
    return CompletableFuture.completedStage(mapper.fromHub(multiVersionHub));
  }
}
