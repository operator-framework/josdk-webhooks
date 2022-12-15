package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

@Configuration
public class AdditionalAdmissionConfig {

  @Bean
  public AdmissionController<Ingress> errorMutatingController() {
    return AdmissionControllers.errorMutatingController();
  }

  @Bean
  public AdmissionController<Ingress> errorValidatingController() {
    return AdmissionControllers.errorValidatingController();
  }

  @Bean
  public AsyncAdmissionController<Ingress> errorAsyncMutatingController() {
    return AdmissionControllers.errorAsyncMutatingController();
  }

  @Bean
  public AsyncAdmissionController<Ingress> errorAsyncValidatingController() {
    return AdmissionControllers.errorAsyncValidatingController();
  }
}
