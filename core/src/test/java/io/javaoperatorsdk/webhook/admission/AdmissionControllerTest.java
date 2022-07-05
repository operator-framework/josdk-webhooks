package io.javaoperatorsdk.webhook.admission;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;

import static io.javaoperatorsdk.webhook.admission.TestCommons.*;

class AdmissionControllerTest {

  @Test
  void validatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          if (resource.getMetadata().getLabels().get(TestCommons.LABEL_KEY) == null) {
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
          resource.getMetadata().getLabels().putIfAbsent(TestCommons.LABEL_KEY, LABEL_TEST_VALUE);
          return resource;
        });
    var inputAdmissionReview = createTestAdmissionReview();

    var response = admissionController.handle(inputAdmissionReview);

    assertMutation(response);
  }
}
