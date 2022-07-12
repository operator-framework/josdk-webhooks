package io.javaoperatorsdk.webhook.conversion;

import org.junit.jupiter.api.Test;

import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2;
import io.javaoperatorsdk.webhook.conversion.mapper.AsyncV1Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.AsyncV2Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV1Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV2Mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

  @Test
  void getsVersionFromApiVersion() {
    assertThat(Utils.versionOfApiVersion("apiextensions.k8s.io/v1")).isEqualTo("v1");
    assertThat(Utils.versionOfApiVersion("extensions/v1beta1")).isEqualTo("v1beta1");
  }

  @Test
  void getMapperResourceType() {
    assertThat(Utils.getFirstTypeArgumentFromInterface(CustomResourceV1Mapper.class, Mapper.class))
        .isEqualTo(CustomResourceV1.class);
    assertThat(Utils.getFirstTypeArgumentFromInterface(CustomResourceV2Mapper.class, Mapper.class))
        .isEqualTo(CustomResourceV2.class);
    assertThat(Utils.getFirstTypeArgumentFromInterface(AsyncV1Mapper.class, AsyncMapper.class))
        .isEqualTo(CustomResourceV1.class);
    assertThat(Utils.getFirstTypeArgumentFromInterface(AsyncV2Mapper.class, AsyncMapper.class))
        .isEqualTo(CustomResourceV2.class);
  }

}
