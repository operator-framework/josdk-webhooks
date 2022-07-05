package io.javaoperatorsdk.webhook.conversion.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;

@TargetVersion("v2")
public class AsyncV2Mapper implements AsyncMapper<CustomResourceV2, CustomResourceV3> {

  CustomResourceV2Mapper mapper = new CustomResourceV2Mapper();

  @Override
  public CompletionStage<CustomResourceV3> toHub(CustomResourceV2 resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<CustomResourceV2> fromHub(CustomResourceV3 customResourceV3) {
    return CompletableFuture.completedStage(mapper.fromHub(customResourceV3));
  }
}
