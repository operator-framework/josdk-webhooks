package io.javaoperatorsdk.webhook.sample.springboot.admission;

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

import static io.javaoperatorsdk.webhook.sample.springboot.admission.AdmissionEndpoint.*;

@Import(AdmissionConfig.class)
@WebFluxTest(AdmissionEndpoint.class)
class AdmissionEndpointTest {

  @Autowired
  private WebTestClient webClient;

  @Value("classpath:admission-request.json")
  private Resource request;

  @Test
  void validation() {
    testValidate(VALIDATE_PATH);
  }

  @Test
  void mutation() {
    testMutate(MUTATE_PATH);
  }

  @Test
  void asyncValidation() {
    testValidate(ASYNC_VALIDATE_PATH);
  }

  @Test
  void asyncMutation() throws Exception {
    testMutate(ASYNC_MUTATE_PATH);
  }

  @Test
  void errorValidation() {
    testInternalServerError(ERROR_VALIDATE_PATH);
  }

  @Test
  void errorMutation() {
    testInternalServerError(ERROR_MUTATE_PATH);
  }

  @Test
  void errorAsyncValidation() {
    testInternalServerError(ERROR_ASYNC_VALIDATE_PATH);
  }

  @Test
  void errorAsyncMutation() {
    testInternalServerError(ERROR_ASYNC_MUTATE_PATH);
  }

  public void testInternalServerError(String path) {
    webClient.post().uri("/" + path).contentType(MediaType.APPLICATION_JSON)
        .body(request())
        .exchange()
        .expectStatus().is5xxServerError();
  }


  public void testMutate(String path) {
    webClient.post().uri("/" + path).contentType(MediaType.APPLICATION_JSON)
        .body(request())
        .exchange()
        .expectStatus().isOk().expectBody().json(
            "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":true,\"patch\":\"W3sib3AiOiJhZGQiLCJwYXRoIjoiL21ldGFkYXRhL2xhYmVscy9hcHAua3ViZXJuZXRlcy5pb34xbmFtZSIsInZhbHVlIjoibXV0YXRpb24tdGVzdCJ9XQ==\",\"patchType\":\"JSONPatch\",\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}");
  }

  public void testValidate(String path) {
    webClient.post().uri("/" + path).contentType(MediaType.APPLICATION_JSON)
        .body(request())
        .exchange()
        .expectStatus().isOk().expectBody().json("{}");
  }

  private BodyInserter<String, ReactiveHttpOutputMessage> request() {
    try {
      return BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath())));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
