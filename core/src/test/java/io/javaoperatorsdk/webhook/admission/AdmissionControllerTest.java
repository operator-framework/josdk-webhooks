package io.javaoperatorsdk.webhook.admission;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

import static io.javaoperatorsdk.webhook.admission.AdmissionTestSupport.LABEL_TEST_VALUE;
import static io.javaoperatorsdk.webhook.admission.AdmissionTestSupport.MISSING_REQUIRED_LABEL;

class AdmissionControllerTest {

  AdmissionTestSupport admissionTestSupport = new AdmissionTestSupport();

  @Test
  void validatesResource() {
    var admissionController = new AdmissionController<HasMetadata>((resource, operation) -> {
    });
    admissionTestSupport.validatesResource(admissionController::handle);
  }

  @Test
  void validatesResource_whenNotAllowedException() {
    var admissionController =
        new AdmissionController<>((Validator<HasMetadata>) (resource, operation) -> {
          throw new NotAllowedException(MISSING_REQUIRED_LABEL);
        });
    admissionTestSupport.notAllowedException(admissionController::handle);
  }

  @Test
  void validatesResource_whenOtherException() {
    var admissionController =
        new AdmissionController<>((Validator<HasMetadata>) (resource, operation) -> {
          throw new IllegalArgumentException("Invalid resource");
        });

    admissionTestSupport.assertThatThrownBy(admissionController::handle)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid resource");
  }

  @Test
  void mutatesResource() {
    var admissionController =
        new AdmissionController<HasMetadata>((resource, operation) -> {
          resource.getMetadata().getLabels().putIfAbsent(AdmissionTestSupport.LABEL_KEY,
              LABEL_TEST_VALUE);
          return resource;
        });
    admissionTestSupport.mutatesResource(admissionController::handle);
  }

  @Test
  void mutatesResource_whenNotAllowedException() {
    var admissionController =
        new AdmissionController<>((Mutator<HasMetadata>) (resource, operation) -> {
          throw new NotAllowedException(MISSING_REQUIRED_LABEL);
        });

    admissionTestSupport.notAllowedException(admissionController::handle);
  }

  @Test
  void mutatesResource_whenOtherException() {
    var admissionController =
        new AdmissionController<>((Mutator<HasMetadata>) (resource, operation) -> {
          throw new IllegalArgumentException("Invalid resource");
        });

    admissionTestSupport.assertThatThrownBy(admissionController::handle)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid resource");
  }
}
