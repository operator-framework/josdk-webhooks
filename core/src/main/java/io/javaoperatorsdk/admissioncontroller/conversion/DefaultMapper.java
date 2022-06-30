package io.javaoperatorsdk.admissioncontroller.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public abstract class DefaultMapper<S extends HasMetadata, T extends HasMetadata>
    implements Mapper<S, T> {

  private final String sourceVersion;
  private final String targetVersion;

  protected DefaultMapper(String sourceVersion, String targetVersion) {
    this.sourceVersion = sourceVersion;
    this.targetVersion = targetVersion;
  }

  @Override
  public String sourceVersion() {
    return sourceVersion;
  }

  @Override
  public String targetVersion() {
    return targetVersion;
  }

}
