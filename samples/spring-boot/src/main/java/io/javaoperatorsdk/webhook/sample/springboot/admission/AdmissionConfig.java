package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

@Configuration
public class AdmissionConfig {

  @Bean
  public AdmissionController<Pod> mutatingController() {
    return AdmissionControllers.mutatingController();
  }

  @Bean
  public AdmissionController<Pod> validatingController() {
    return AdmissionControllers.validatingController();
  }

  @Bean
  public AsyncAdmissionController<Pod> asyncMutatingController() {
    return AdmissionControllers.asyncMutatingController();
  }

  @Bean
  public AsyncAdmissionController<Pod> asyncValidatingController() {
    return AdmissionControllers.asyncValidatingController();
  }
}
