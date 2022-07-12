package io.javaoperatorsdk.webhook.sample.springboot.conversion;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.sample.commons.customresource.TestCustomResourceV2;

import static io.javaoperatorsdk.webhook.sample.commons.mapper.V1Mapper.DEFAULT_ADDITIONAL_VALUE;
import static io.javaoperatorsdk.webhook.sample.springboot.conversion.ConversionEndpoint.ASYNC_CONVERSION_PATH;
import static io.javaoperatorsdk.webhook.sample.springboot.conversion.ConversionEndpoint.CONVERSION_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@Import(ConversionConfig.class)
@WebFluxTest(ConversionEndpoint.class)
class ConversionEndpointTest {

  @Autowired
  private WebTestClient webClient;

  @Value("classpath:conversion-request.json")
  private Resource request;

  @Test
  void convert() {
    testConversion(CONVERSION_PATH);
  }

  @Test
  void asyncConvert() {
    testConversion(ASYNC_CONVERSION_PATH);
  }

  public void testConversion(String path) {
    webClient.post().uri("/" + path).contentType(MediaType.APPLICATION_JSON)
        .body(request())
        .exchange()
        .expectStatus().isOk().expectBody(ConversionReview.class).consumeWith(res -> {
          var review = res.getResponseBody();
          var resource1 =
              ((TestCustomResourceV2) review.getResponse().getConvertedObjects().get(0));
          assertThat(review.getResponse().getConvertedObjects()).hasSize(2);
          assertThat(resource1.getSpec().getAdditionalValue()).isEqualTo(DEFAULT_ADDITIONAL_VALUE);
          assertThat(resource1.getMetadata().getName()).isEqualTo("resource1");
        });
  }

  private BodyInserter<String, ReactiveHttpOutputMessage> request() {
    try {
      return BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath())));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
