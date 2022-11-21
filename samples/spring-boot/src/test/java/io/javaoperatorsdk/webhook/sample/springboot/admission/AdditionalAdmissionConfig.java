package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

@Configuration
public class AdditionalAdmissionConfig {

  @Bean
  public AdmissionController<Pod> errorMutatingController() {
    return AdmissionControllers.errorMutatingController();
  }

  @Bean
  public AdmissionController<Pod> errorValidatingController() {
    return AdmissionControllers.errorValidatingController();
  }

  @Bean
  public AsyncAdmissionController<Pod> errorAsyncMutatingController() {
    return AdmissionControllers.errorAsyncMutatingController();
  }

  @Bean
  public AsyncAdmissionController<Pod> errorAsyncValidatingController() {
    return AdmissionControllers.errorAsyncValidatingController();
  }
}
