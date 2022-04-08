package io.javaoperatorsdk.admissioncontroller.sample.springboot;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Import(Config.class)
@WebFluxTest(AdmissionEndpoint.class)
class AdmissionEndpointTest {

  @Autowired
  private WebTestClient webClient;

  @Value("classpath:admission-request.json")
  private Resource request;

  @Test
  void validation() throws Exception {
    webClient.post().uri("/validate").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath()))))
        .exchange()
        .expectStatus().isOk().expectBody().json("{}");
  }

  @Test
  void mutation() throws Exception {
    webClient.post().uri("/mutate").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath()))))
        .exchange()
        .expectStatus().isOk().expectBody().json(
            "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":true,\"patch\":\"W3sib3AiOiJhZGQiLCJwYXRoIjoiL21ldGFkYXRhL2xhYmVscy9hcHAua3ViZXJuZXRlcy5pb34xbmFtZSIsInZhbHVlIjoibXV0YXRpb24tdGVzdCJ9XQ==\",\"patchType\":\"JSONPatch\",\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}");
  }

  @Test
  void asyncValidation() throws Exception {
    webClient.post().uri("/async-validate").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath()))))
        .exchange()
        .expectStatus().isOk().expectBody().json("{}");
  }

  @Test
  void asyncMutation() throws Exception {
    webClient.post().uri("/async-mutate").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath()))))
        .exchange()
        .expectStatus().isOk().expectBody().json(
            "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":true,\"patch\":\"W3sib3AiOiJhZGQiLCJwYXRoIjoiL21ldGFkYXRhL2xhYmVscy9hcHAua3ViZXJuZXRlcy5pb34xbmFtZSIsInZhbHVlIjoibXV0YXRpb24tdGVzdCJ9XQ==\",\"patchType\":\"JSONPatch\",\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}");
  }

}
