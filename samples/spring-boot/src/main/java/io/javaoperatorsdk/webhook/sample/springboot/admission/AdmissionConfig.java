package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers;

@Configuration
public class AdmissionConfig {

  @Bean
  public AdmissionController<Ingress> mutatingController() {
    return AdmissionControllers.mutatingController();
  }

  @Bean
  public AdmissionController<Ingress> validatingController() {
    return AdmissionControllers.validatingController();
  }

  @Bean
  public AsyncAdmissionController<Ingress> asyncMutatingController() {
    return AdmissionControllers.asyncMutatingController();
  }

  @Bean
  public AsyncAdmissionController<Ingress> asyncValidatingController() {
    return AdmissionControllers.asyncValidatingController();
  }
}
