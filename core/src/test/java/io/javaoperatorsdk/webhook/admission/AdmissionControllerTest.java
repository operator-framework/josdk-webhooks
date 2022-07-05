package io.javaoperatorsdk.webhook.admission;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;

import static io.javaoperatorsdk.webhook.admission.AdmissionTestSupport.*;

class AdmissionControllerTest {

  AdmissionTestSupport admissionTestSupport = new AdmissionTestSupport();

  @Test
  void validatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          if (resource.getMetadata().getLabels().get(AdmissionTestSupport.LABEL_KEY) == null) {
            throw new NotAllowedException(MISSING_REQUIRED_LABEL);
          }
        });
    admissionTestSupport.validatesResource(admissionController::handle);
  }

  @Test
  void mutatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          resource.getMetadata().getLabels().putIfAbsent(AdmissionTestSupport.LABEL_KEY,
              LABEL_TEST_VALUE);
          return resource;
        });
    admissionTestSupport.mutatesResource(admissionController::handle);
  }
}
