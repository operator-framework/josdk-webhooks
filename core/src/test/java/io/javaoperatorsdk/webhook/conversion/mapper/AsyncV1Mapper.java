package io.javaoperatorsdk.webhook.conversion.mapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.javaoperatorsdk.webhook.conversion.AsyncMapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;

@TargetVersion("v1")
public class AsyncV1Mapper implements AsyncMapper<CustomResourceV1, CustomResourceV3> {

  CustomResourceV1Mapper mapper = new CustomResourceV1Mapper();

  @Override
  public CompletionStage<CustomResourceV3> toHub(CustomResourceV1 resource) {
    return CompletableFuture.completedStage(mapper.toHub(resource));
  }

  @Override
  public CompletionStage<CustomResourceV1> fromHub(CustomResourceV3 customResourceV3) {
    return CompletableFuture.completedStage(mapper.fromHub(customResourceV3));
  }
}
