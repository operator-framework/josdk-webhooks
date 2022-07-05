package io.javaoperatorsdk.webhook.conversion;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionResponse;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV1Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV2Mapper;
import io.javaoperatorsdk.webhook.conversion.mapper.CustomResourceV3Mapper;

public class ConversionControllerTest {
  ConversionTests conversionTests = new ConversionTests();
  ConversionController controller = new ConversionController();

  @BeforeEach
  void setup() {
    controller.registerMapper(new CustomResourceV1Mapper());
    controller.registerMapper(new CustomResourceV2Mapper());
    controller.registerMapper(new CustomResourceV3Mapper());
  }

  @Test
  void handlesSimpleConversion() {
    conversionTests.handlesSimpleConversion(getConversionReviewConversionResponseFunction());
  }

  @Test
  void convertsVariousVersionsInSingleRequest() {
    conversionTests
        .convertsVariousVersionsInSingleRequest(getConversionReviewConversionResponseFunction());
  }

  @Test
  void errorResponseOnMissingMapper() {
    conversionTests
        .convertsVariousVersionsInSingleRequest(getConversionReviewConversionResponseFunction());
  }

  private Function<ConversionReview, ConversionResponse> getConversionReviewConversionResponseFunction() {
    return request -> controller.handle(request).getResponse();
  }

}
