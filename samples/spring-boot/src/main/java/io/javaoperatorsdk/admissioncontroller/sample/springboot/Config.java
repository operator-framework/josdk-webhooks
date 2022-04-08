package io.javaoperatorsdk.admissioncontroller.sample.springboot;

import java.util.concurrent.CompletableFuture;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import io.javaoperatorsdk.admissioncontroller.AsyncAdmissionController;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.mutation.AsyncMutator;
import io.javaoperatorsdk.admissioncontroller.mutation.Mutator;
import io.javaoperatorsdk.admissioncontroller.validation.Validator;

@Configuration
public class Config {

  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";
  public static final String ERROR_MESSAGE = "Some error happened";

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
  public AsyncAdmissionController<Pod> asyncMutatingController() {
    return new AsyncAdmissionController<>(
        (AsyncMutator<Pod>) (resource, operation) -> CompletableFuture.supplyAsync(() -> {
          resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
          return resource;
        }));
  }

  @Bean
  public AsyncAdmissionController<Pod> asyncValidatingController() {
    return new AsyncAdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
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
