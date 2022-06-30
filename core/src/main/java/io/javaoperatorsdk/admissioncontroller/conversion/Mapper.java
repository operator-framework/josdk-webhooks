package io.javaoperatorsdk.admissioncontroller.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public interface Mapper<S extends HasMetadata, T extends HasMetadata> {

  String sourceVersion();

  String targetVersion();

  T map(S source);

}
