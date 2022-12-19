package io.javaoperatorsdk.webhook.sample.springboot.conversion;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.ASYNC_CONVERSION_PATH;
import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.CONVERSION_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@Import(ConversionConfig.class)
@WebFluxTest(ConversionEndpoint.class)
class ConversionEndpointTest {

  @Autowired
  private WebTestClient webClient;

  @Test
  void convert() {
    testConversion(CONVERSION_PATH);
  }

  @Test
  void asyncConvert() {
    testConversion(ASYNC_CONVERSION_PATH);
  }

  @Test
  void errorConversion() {
    testErrorConversion(CONVERSION_PATH);
  }

  @Test
  void asyncErrorConversion() {
    testErrorConversion(ASYNC_CONVERSION_PATH);
  }

  private void testErrorConversion(String conversionPath) {
    webClient.post().uri("/" + conversionPath).contentType(MediaType.APPLICATION_JSON)
        .body(errorRequest())
        .exchange()
        .expectStatus().is5xxServerError();
  }

  public void testConversion(String path) {
    webClient.post().uri("/" + path).contentType(MediaType.APPLICATION_JSON)
        .body(request())
        .exchange()
        .expectStatus().isOk().expectBody(ConversionReview.class).consumeWith(res -> {
          var review = res.getResponseBody();
          var resource1 =
              ((MultiVersionCustomResourceV2) review.getResponse().getConvertedObjects().get(0));
          assertThat(review.getResponse().getConvertedObjects()).hasSize(2);
          assertThat(resource1.getMetadata().getName()).isEqualTo("resource1");
        });
  }

  private BodyInserter<String, ReactiveHttpOutputMessage> request() {
    return requestFromResource("/conversion-request.json");
  }

  private BodyInserter<String, ReactiveHttpOutputMessage> errorRequest() {
    return requestFromResource("/conversion-error-request.json");
  }

  private BodyInserter<String, ReactiveHttpOutputMessage> requestFromResource(String resource) {
    try {
      ClassPathResource classPathResource = new ClassPathResource(resource);
      return BodyInserters
          .fromValue(new String(FileCopyUtils.copyToByteArray(classPathResource.getInputStream())));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
