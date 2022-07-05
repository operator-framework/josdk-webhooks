package io.javaoperatorsdk.webhook.conversion;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionResponse;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;

import static io.javaoperatorsdk.webhook.conversion.Commons.FAILED_STATUS_MESSAGE;
import static io.javaoperatorsdk.webhook.conversion.TestCommons.*;
import static org.assertj.core.api.Assertions.assertThat;

class ConversionTests {


  void handlesSimpleConversion(Function<ConversionReview, ConversionResponse> func) {
    var request = createRequest(V2, v1resource());

    var response = func.apply(request);

    assertThat(response.getConvertedObjects()).hasSize(1);
    assertThat(response.getUid()).isEqualTo(request.getRequest().getUid());
    CustomResourceV2 convertedObject = (CustomResourceV2) response.getConvertedObjects().get(0);
    assertThat(convertedObject.getMetadata()).isEqualTo(v1resource().getMetadata());
    assertThat(convertedObject.getSpec().getAdditionalValue()).isEqualTo(DEFAULT_ADDITIONAL_VALUE);
    assertThat(convertedObject.getSpec().getValue()).isEqualTo(String.valueOf(VALUE));
  }

  void convertsVariousVersionsInSingleRequest(Function<ConversionReview, ConversionResponse> func) {
    var request = createRequest(V3, v1resource(), v2resource(), v3resource());

    var response = func.apply(request);

    assertThat(response.getConvertedObjects()).hasSize(3);
    List<String> namesInOrder = response.getConvertedObjects().stream()
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



}
