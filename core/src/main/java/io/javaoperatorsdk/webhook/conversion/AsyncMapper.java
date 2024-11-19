package io.javaoperatorsdk.webhook.conversion;

import java.util.concurrent.CompletionStage;

import io.fabric8.kubernetes.api.model.HasMetadata;

public interface AsyncMapper<R extends HasMetadata, HUB> {

  CompletionStage<HUB> toHub(R resource);

  CompletionStage<R> fromHub(HUB hub);
}
