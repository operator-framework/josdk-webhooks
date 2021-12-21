package io.javaoperatorsdk.admissioncontroller.sample.springbootsample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;

@Configuration
public class Config {

  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";

  @Bean
  public AdmissionController<Pod> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
      return resource;
    });
  }

  @Bean
  public AdmissionController<Pod> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

}
