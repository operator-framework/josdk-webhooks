package io.javaoperatorsdk.webhook.admission.sample.quarkus.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.javaoperatorsdk.webhook.admission.sample.quarkus.conversion.ConversionEndpoint.ASYNC_CONVERSION_PATH;
import static io.javaoperatorsdk.webhook.admission.sample.quarkus.conversion.ConversionEndpoint.CONVERSION_PATH;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ConversionEndpointTest {

  @Test
  void conversion() {
    testConversion(CONVERSION_PATH);
  }

  @Test
  void asyncConversion() {
    testConversion(ASYNC_CONVERSION_PATH);
  }

  public void testConversion(String path) {
    given().contentType(ContentType.JSON)
        .body(jsonRequest())
        .when().post("/" + path)
        .then()
        .statusCode(200)
        .body(is("s"));
  }

  private String jsonRequest() {
    try (InputStream is = this.getClass().getResourceAsStream("/conversion-request.json")) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
