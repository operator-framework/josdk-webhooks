package io.javaoperatorsdk.webhook.conversion;

import io.fabric8.kubernetes.api.model.HasMetadata;

public interface Mapper<R extends HasMetadata, HUB> {
  // todo version with annotation ?
  /**
   * The target version of the resource this mapper supports. Example values: "v1","v1beta1". This
   * is not the full API Version just the version suffix, for example only the "v1" of api version:
   * "apiextensions.k8s.io/v1"
   *
   **/
  String version();

  HUB toHub(R resource);

  R fromHub(HUB hub);

}
