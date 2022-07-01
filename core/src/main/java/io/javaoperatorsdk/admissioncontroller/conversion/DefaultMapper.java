package io.javaoperatorsdk.admissioncontroller.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public abstract class DefaultMapper<S extends HasMetadata, T extends HasMetadata>
    implements Mapper<S, T> {

  private final String version;

  protected DefaultMapper(String version) {
    this.version = version;
  }

  @Override
  public String version() {
    return version;
  }

}
