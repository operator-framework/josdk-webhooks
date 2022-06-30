package io.javaoperatorsdk.admissioncontroller;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.admissioncontroller.admission.AdmissionController;
import io.javaoperatorsdk.admissioncontroller.admission.NotAllowedException;

import static io.javaoperatorsdk.admissioncontroller.Commons.*;

class AdmissionControllerTest {

  @Test
  void validatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          if (resource.getMetadata().getLabels().get(Commons.LABEL_KEY) == null) {
            throw new NotAllowedException(MISSING_REQUIRED_LABEL);
          }
        });
    var inputAdmissionReview = createTestAdmissionReview();

    var response = admissionController.handle(inputAdmissionReview);

    assertValidation(response, inputAdmissionReview.getRequest().getUid());
  }

  @Test
  void mutatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          resource.getMetadata().getLabels().putIfAbsent(Commons.LABEL_KEY, LABEL_TEST_VALUE);
          return resource;
        });
    var inputAdmissionReview = createTestAdmissionReview();

    var response = admissionController.handle(inputAdmissionReview);

    assertMutation(response);
  }
}
