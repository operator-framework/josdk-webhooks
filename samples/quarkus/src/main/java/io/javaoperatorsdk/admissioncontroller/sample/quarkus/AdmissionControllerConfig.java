package io.javaoperatorsdk.admissioncontroller.sample.quarkus;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.inject.Singleton;

import io.fabric8.kubernetes.api.model.Pod;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;

@Dependent
public class AdmissionControllerConfig {

  public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";

  @Singleton
  @Named("mutatingController")
  public AdmissionController<Pod> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
      return resource;
    });
  }

  @Singleton
  @Named("validatingController")
  public AdmissionController<Pod> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

}
