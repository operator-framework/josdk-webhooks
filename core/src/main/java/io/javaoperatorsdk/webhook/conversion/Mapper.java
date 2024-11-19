package io.javaoperatorsdk.webhook.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public interface Mapper<R extends HasMetadata, HUB> {

  HUB toHub(R resource);

  R fromHub(HUB hub);
}
