package io.javaoperatorsdk.webhook.conversion;

import java.util.Arrays;
import java.util.UUID;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionRequest;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.crd.*;

public class TestCommons {

  public static final String DEFAULT_ADDITIONAL_VALUE = "defaultAdditionalValue";
  public static final String DEFAULT_THIRD_VALUE = "defaultThirdValue";
  public static final String V1 = "v1";
  public static final String V2 = "v2";
  public static final String V3 = "v3";
  public static final int VALUE = 1;
  public static final String V1_NAME = "v1name";
  public static final String V2_NAME = "v2name";
  public static final String V3_NAME = "v3name";
  public static final String API_GROUP = "sample.javaoperatorsdk";

  public static ConversionReview createRequest(String targetVersion, HasMetadata... resources) {
    ConversionReview review = new ConversionReview();
    ConversionRequest request = new ConversionRequest();
    request.setDesiredAPIVersion(API_GROUP + "/" + targetVersion);
    request.setUid(UUID.randomUUID().toString());
    request.setObjects(Arrays.asList(resources));
    review.setRequest(request);
    return review;
  }

  public static CustomResourceV1 v1resource() {
    var r = new CustomResourceV1();
    r.setMetadata(new ObjectMetaBuilder()
        .withName(V1_NAME).withNamespace("default")
        .build());
    r.setSpec(new CustomResourceV1Spec());
    r.getSpec().setValue(VALUE);
    return r;
  }

  public static CustomResourceV2 v2resource() {
    var r = new CustomResourceV2();
    r.setMetadata(new ObjectMetaBuilder()
        .withName(V2_NAME).withNamespace("default")
        .build());
    r.setSpec(new CustomResourceV2Spec());
    r.getSpec().setValue("2");
    r.getSpec().setAdditionalValue("additionalValueV2");
    return r;
  }

  public static CustomResourceV3 v3resource() {
    var r = new CustomResourceV3();
    r.setMetadata(new ObjectMetaBuilder()
        .withName(V3_NAME).withNamespace("default")
        .build());
    r.setSpec(new CustomResourceV3Spec());
    r.getSpec().setValue("3");
    r.getSpec().setAdditionalValue("additionalValueV3");
    r.getSpec().setThirdValue("thirdValue");
    return r;
  }

}
