package io.javaoperatorsdk.webhook.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public interface Mapper<R extends HasMetadata, T> {

  String version();

  T toHub(R source);

  R fromHub(T source);

}
