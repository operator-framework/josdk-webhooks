package io.javaoperatorsdk.webhook.sample.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.ASYNC_CONVERSION_PATH;
import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.CONVERSION_PATH;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ConversionEndpointTest {

  final static String expectedResult =
      "{\"apiVersion\":\"apiextensions.k8s.io/v1\",\"kind\":\"ConversionReview\",\"response\":{\"convertedObjects\":[{\"apiVersion\":\"sample.javaoperatorsdk/v2\",\"kind\":\"MultiVersionCustomResource\",\"metadata\":{\"creationTimestamp\":\"2021-09-04T14:03:02Z\",\"name\":\"resource1\",\"namespace\":\"default\",\"resourceVersion\":\"143\",\"uid\":\"3415a7fc-162b-4300-b5da-fd6083580d66\"},\"spec\":{\"alteredValue\":\"1\"}},{\"apiVersion\":\"sample.javaoperatorsdk/v2\",\"kind\":\"MultiVersionCustomResource\",\"metadata\":{\"creationTimestamp\":\"2021-09-04T14:03:02Z\",\"name\":\"resource2\",\"namespace\":\"default\",\"resourceVersion\":\"14344\",\"uid\":\"1115a7fc-162b-4300-b5da-fd6083580d55\"},\"spec\":{\"alteredValue\":\"2\"}}],\"result\":{\"apiVersion\":\"v1\",\"kind\":\"Status\",\"status\":\"Success\"},\"uid\":\"705ab4f5-6393-11e8-b7cc-42010a800002\"}}";

  @Test
  void conversion() {
    testConversion(CONVERSION_PATH);
  }

  @Test
  void asyncConversion() {
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
    given().contentType(ContentType.JSON)
        .body(errorRequest())
        .when().post("/" + conversionPath)
        .then()
        .statusCode(500);
  }

  public void testConversion(String path) {
    given().contentType(ContentType.JSON)
        .body(request())
        .when().post("/" + path)
        .then()
        .statusCode(200)
        .body(is(expectedResult));
  }

  private String errorRequest() {
    return request("/conversion-error-request.json");
  }

  private String request() {
    return request("/conversion-request.json");
  }

  private String request(String path) {
    try (InputStream is = this.getClass().getResourceAsStream(path)) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
