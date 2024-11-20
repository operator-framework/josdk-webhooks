package io.javaoperatorsdk.webhook.conversion;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionRequest;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionResponse;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1Spec;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2Spec;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3Spec;

import static io.javaoperatorsdk.webhook.conversion.Commons.FAILED_STATUS_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class ConversionTestSupport {

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

  void handlesSimpleConversion(Function<ConversionReview, ConversionResponse> func) {
    var request = createRequest(V2, v1resource());
    var response = func.apply(request);

    assertThat(response.getConvertedObjects()).hasSize(1);
    assertThat(response.getUid()).isEqualTo(request.getRequest().getUid());
    var convertedObject = (CustomResourceV2) response.getConvertedObjects().get(0);
    assertThat(convertedObject.getMetadata()).isEqualTo(v1resource().getMetadata());
    assertThat(convertedObject.getSpec().getAdditionalValue()).isEqualTo(DEFAULT_ADDITIONAL_VALUE);
    assertThat(convertedObject.getSpec().getValue()).isEqualTo(String.valueOf(VALUE));
  }

  void convertsVariousVersionsInSingleRequest(Function<ConversionReview, ConversionResponse> func) {
    var request = createRequest(V3, v1resource(), v2resource(), v3resource());
    var response = func.apply(request);

    assertThat(response.getConvertedObjects()).hasSize(3);
    var namesInOrder = response.getConvertedObjects().stream()
        .map(HasMetadata.class::cast)
        .map(r -> r.getMetadata().getName()).collect(Collectors.toList());
    assertThat(namesInOrder).containsExactly(V1_NAME, V2_NAME, V3_NAME);
    assertThat(response.getConvertedObjects()).allMatch(r -> r instanceof CustomResourceV3);
  }

  void errorResponseOnMissingMapper(Function<ConversionReview, ConversionResponse> func) {
    var request = createRequest("v4", v1resource());
    var response = func.apply(request);

    assertThat(response.getUid()).isEqualTo(request.getRequest().getUid());
    assertThat(response.getResult().getStatus()).isEqualTo(FAILED_STATUS_MESSAGE);
    assertThat(response.getResult().getMessage()).contains("Missing", "v4");
  }

  public static ConversionReview createRequest(String targetVersion, HasMetadata... resources) {
    var review = new ConversionReview();
    var request = new ConversionRequest();
    request.setDesiredAPIVersion(API_GROUP + "/" + targetVersion);
    request.setUid(UUID.randomUUID().toString());
    request.setObjects(Arrays.asList(resources));
    review.setRequest(request);
    return review;
  }

  public static CustomResourceV1 v1resource() {
    var resourceV1 = new CustomResourceV1();
    resourceV1.setMetadata(new ObjectMetaBuilder()
        .withName(V1_NAME).withNamespace("default")
        .build());
    resourceV1.setSpec(new CustomResourceV1Spec());
    resourceV1.getSpec().setValue(VALUE);
    return resourceV1;
  }

  public static CustomResourceV2 v2resource() {
    var resourceV2 = new CustomResourceV2();
    resourceV2.setMetadata(new ObjectMetaBuilder()
        .withName(V2_NAME).withNamespace("default")
        .build());
    resourceV2.setSpec(new CustomResourceV2Spec());
    resourceV2.getSpec().setValue("2");
    resourceV2.getSpec().setAdditionalValue("additionalValueV2");
    return resourceV2;
  }

  public static CustomResourceV3 v3resource() {
    var resourceV3 = new CustomResourceV3();
    resourceV3.setMetadata(new ObjectMetaBuilder()
        .withName(V3_NAME).withNamespace("default")
        .build());
    resourceV3.setSpec(new CustomResourceV3Spec());
    resourceV3.getSpec().setValue("3");
    resourceV3.getSpec().setAdditionalValue("additionalValueV3");
    resourceV3.getSpec().setThirdValue("thirdValue");
    return resourceV3;
  }
}
