package io.javaoperatorsdk.admissioncontroller.sample.springbootsample;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdmissionEndpointTest {

  @Autowired
  private MockMvc mockMvc;

  @Value("classpath:admission-request.json")
  private Resource request;


  @Test
  public void testValidation() throws Exception {
    mockMvc.perform(post("/validate").contentType(MediaType.APPLICATION_JSON)
        .content(new String(Files.readAllBytes(request.getFile().toPath()))))
        .andExpectAll(status().isOk(), content().json("{}"));
  }

  @Test
  public void testMutation() throws Exception {
    mockMvc.perform(post("/mutate").contentType(MediaType.APPLICATION_JSON)
        .content(new String(Files.readAllBytes(request.getFile().toPath()))))
        .andExpectAll(content().contentType(MediaType.APPLICATION_JSON),
            content().json(
                "{\"apiVersion\":\"admission.k8s.io/v1\",\"kind\":\"AdmissionReview\",\"response\":{\"allowed\":true,\"patch\":\"W3sib3AiOiJhZGQiLCJwYXRoIjoiL21ldGFkYXRhL2xhYmVscy9hcHAua3ViZXJuZXRlcy5pb34xbmFtZSIsInZhbHVlIjoibXV0YXRpb24tdGVzdCJ9XQ==\",\"patchType\":\"JSONPatch\",\"uid\":\"0df28fbd-5f5f-11e8-bc74-36e6bb280816\"}}"));
  }

}
