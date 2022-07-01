package io.javaoperatorsdk.webhook.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public abstract class AbstractMapper<S extends HasMetadata, HUB>
    implements Mapper<S, HUB> {

  private final String version;

  protected AbstractMapper(String version) {
    this.version = version;
  }

  @Override
  public String version() {
    return version;
  }

}
