package io.javaoperatorsdk.webhook.sample.springboot.admission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

@Configuration
public class AdditionalAdmissionConfig {

  public static final String ERROR_MESSAGE = "Some error happened";

  @Bean
  public AdmissionController<Pod> errorMutatingController() {
    return new AdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  @Bean
  public AdmissionController<Pod> errorValidatingController() {
    return new AdmissionController<>((Mutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  @Bean
  public AsyncAdmissionController<Pod> errorAsyncMutatingController() {
    return new AsyncAdmissionController<>((AsyncMutator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }

  @Bean
  public AsyncAdmissionController<Pod> errorAsyncValidatingController() {
    return new AsyncAdmissionController<>((Validator<Pod>) (resource, operation) -> {
      throw new IllegalStateException(ERROR_MESSAGE);
    });
  }
}
