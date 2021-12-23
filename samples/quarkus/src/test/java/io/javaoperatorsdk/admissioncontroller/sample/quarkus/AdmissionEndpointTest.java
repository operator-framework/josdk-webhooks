package io.javaoperatorsdk.admissioncontroller.sample.quarkus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AdmissionEndpointTest {

  @Test
  public void mutates() {
    given().contentType(ContentType.JSON)
        .body(jsonRequest())
        .when().post("/mutate")
        .then()
        .statusCode(200)
        .body(is(
            "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":true,\"patch\":\"W3sib3AiOiJhZGQiLCJwYXRoIjoiL21ldGFkYXRhL2xhYmVscy9hcHAua3ViZXJuZXRlcy5pb34xbmFtZSIsInZhbHVlIjoibXV0YXRpb24tdGVzdCJ9XQ==\",\"patchType\":\"JSONPatch\",\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}"));
  }

  @Test
  public void validates() {
    given().contentType(ContentType.JSON)
        .body(jsonRequest())
        .when().post("/validate")
        .then()
        .statusCode(200)
        .body(is(
            "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":false,\"status\":{\"apiVersion\":\"v1\",\"kind\":\"Status\",\"code\":403,\"message\":\"Missing label: app.kubernetes.io/name\"},\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}"));
  }

  private String jsonRequest() {
    try (InputStream is = this.getClass().getResourceAsStream("/admission-request.json")) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
