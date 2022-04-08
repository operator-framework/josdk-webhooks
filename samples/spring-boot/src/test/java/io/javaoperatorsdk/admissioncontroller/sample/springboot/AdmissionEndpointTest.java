package io.javaoperatorsdk.admissioncontroller.sample.springboot;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ContextConfiguration(classes = Config.class)
@WebFluxTest(AdmissionEndpoint.class)
class AdmissionEndpointTest {

  @Autowired
  private WebTestClient webClient;

  @Value("classpath:admission-request.json")
  private Resource request;

  @Test
  public void testValidation() throws Exception {
    webClient.post().uri("/validate").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new String(Files.readAllBytes(request.getFile().toPath()))))
        .exchange()
        .expectStatus().isOk().expectBody().json("{}");
  }

  // @Test
  // public void testMutation() throws Exception {
  // mockMvc.perform(post("/mutate").contentType(MediaType.APPLICATION_JSON)
  // .content(new String(Files.readAllBytes(request.getFile().toPath()))))
  // .andExpectAll(content().contentType(MediaType.APPLICATION_JSON),
  // content().json(
  // "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":true,\"patch\":\"W3sib3AiOiJhZGQiLCJwYXRoIjoiL21ldGFkYXRhL2xhYmVscy9hcHAua3ViZXJuZXRlcy5pb34xbmFtZSIsInZhbHVlIjoibXV0YXRpb24tdGVzdCJ9XQ==\",\"patchType\":\"JSONPatch\",\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}"));
  // }

}
