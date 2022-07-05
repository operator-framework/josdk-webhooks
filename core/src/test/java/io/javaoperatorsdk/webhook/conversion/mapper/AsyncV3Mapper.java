package io.javaoperatorsdk.webhook.conversion.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;

@TargetVersion("v3")
public class AsyncV3Mapper implements AsyncMapper<CustomResourceV3, CustomResourceV3> {

  CustomResourceV3Mapper mapper = new CustomResourceV3Mapper();

  @Override
  public CompletionStage<CustomResourceV3> toHub(CustomResourceV3 resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<CustomResourceV3> fromHub(CustomResourceV3 customResourceV3) {
    return CompletableFuture.completedStage(mapper.fromHub(customResourceV3));
  }
}
