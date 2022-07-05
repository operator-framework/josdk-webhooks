package io.javaoperatorsdk.webhook.conversion;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionResponse;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.mapper.*;

class AsyncConversionControllerTest {

  ConversionTestSupport conversionTestSupport = new ConversionTestSupport();
  AsyncConversionController controller = new AsyncConversionController();

  @BeforeEach
  void setup() {
    controller.registerMapper(new AsyncV1Mapper());
    controller.registerMapper(new AsyncV2Mapper());
    controller.registerMapper(new AsyncV3Mapper());
  }

  @Test
  void handlesSimpleConversion() {
    conversionTestSupport.handlesSimpleConversion(getConversionReviewConversionResponseFunction());
  }


  @Test
  void convertsVariousVersionsInSingleRequest() {
    conversionTestSupport
        .convertsVariousVersionsInSingleRequest(getConversionReviewConversionResponseFunction());
  }

  @Test
  void errorResponseOnMissingMapper() {
    conversionTestSupport
        .errorResponseOnMissingMapper(getConversionReviewConversionResponseFunction());
  }

  private Function<ConversionReview, ConversionResponse> getConversionReviewConversionResponseFunction() {
    return request -> {
      try {
        return controller.handle(request).toCompletableFuture().get().getResponse();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    };
  }

}
