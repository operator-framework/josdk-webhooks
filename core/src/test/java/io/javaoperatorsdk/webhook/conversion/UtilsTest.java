package io.javaoperatorsdk.webhook.conversion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

  @Test
  void getsVersionFromApiVersion() {
    assertThat(Utils.versionOfApiVersion("apiextensions.k8s.io/v1")).isEqualTo("v1");
    assertThat(Utils.versionOfApiVersion("extensions/v1beta1")).isEqualTo("v1beta1");
  }
}
