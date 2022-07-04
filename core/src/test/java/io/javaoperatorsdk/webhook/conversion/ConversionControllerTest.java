package io.javaoperatorsdk.webhook.conversion;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionRequest;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.crd.*;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV1Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV2Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV3Mapper;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionControllerTest {

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

  ConversionController controller = new ConversionController();

  @BeforeEach
  void setup() {
    controller.registerMapper(new CustomResourceV1Mapper());
    controller.registerMapper(new CustomResourceV2Mapper());
    controller.registerMapper(new CustomResourceV3Mapper());
  }

  @Test
  void handlesSimpleConversion() {
    var request = createRequest(V2, v1resource());

    var response = controller.handle(request).getResponse();

    assertThat(response.getConvertedObjects()).hasSize(1);
    assertThat(response.getUid()).isEqualTo(request.getRequest().getUid());
    CustomResourceV2 convertedObject = (CustomResourceV2) response.getConvertedObjects().get(0);
    assertThat(convertedObject.getMetadata()).isEqualTo(v1resource().getMetadata());
    assertThat(convertedObject.getSpec().getAdditionalValue()).isEqualTo(DEFAULT_ADDITIONAL_VALUE);
    assertThat(convertedObject.getSpec().getValue()).isEqualTo(String.valueOf(VALUE));
  }

  @Test
  void convertsVariousVersionsInSingleRequest() {
    var request = createRequest(V3, v1resource(), v2resource(), v3resource());

    var response = controller.handle(request).getResponse();

    assertThat(response.getConvertedObjects()).hasSize(3);
    List<String> namesInOrder = response.getConvertedObjects().stream()
        .map(r -> r.getMetadata().getName()).collect(Collectors.toList());
    assertThat(namesInOrder).containsExactly(V1_NAME, V2_NAME, V3_NAME);
    assertThat(response.getConvertedObjects()).allMatch(r -> r instanceof CustomResourceV3);
  }

  ConversionReview createRequest(String targetVersion, HasMetadata... resources) {
    ConversionReview review = new ConversionReview();
    ConversionRequest request = new ConversionRequest();
    request.setDesiredAPIVersion(API_GROUP + "/" + targetVersion);
    request.setUid(UUID.randomUUID().toString());
    request.setObjects(Arrays.asList(resources));
    review.setRequest(request);
    return review;
  }

  CustomResourceV1 v1resource() {
    var r = new CustomResourceV1();
    r.setMetadata(new ObjectMetaBuilder()
        .withName(V1_NAME).withNamespace("default")
        .build());
    r.setSpec(new CustomResourceV1Spec());
    r.getSpec().setValue(VALUE);
    return r;
  }

  CustomResourceV2 v2resource() {
    var r = new CustomResourceV2();
    r.setMetadata(new ObjectMetaBuilder()
        .withName(V2_NAME).withNamespace("default")
        .build());
    r.setSpec(new CustomResourceV2Spec());
    r.getSpec().setValue("2");
    r.getSpec().setAdditionalValue("additionalValueV2");
    return r;
  }

  CustomResourceV3 v3resource() {
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
