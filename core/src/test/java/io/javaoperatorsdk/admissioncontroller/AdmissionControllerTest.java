package io.javaoperatorsdk.admissioncontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;

import static org.assertj.core.api.Assertions.assertThat;

class AdmissionControllerTest {

  public static final String MISSING_REQUIRED_LABEL = "Missing required label.";
  public static final String LABEL_KEY = "app.kubernetes.io/name";
  public static final String LABEL_TEST_VALUE = "mutation-test";

  @Test
  public void validatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          if (resource.getMetadata().getLabels().get(LABEL_KEY) == null) {
            throw new NotAllowedException(MISSING_REQUIRED_LABEL);
          }
        });
    var inputAdmissionReview = createTestAdmissionReview();

    var response = admissionController.handle(inputAdmissionReview);

    assertThat(response.getResponse().getUid())
        .isEqualTo(inputAdmissionReview.getRequest().getUid());
    assertThat(response.getResponse().getStatus().getCode()).isEqualTo(403);
    assertThat(response.getResponse().getStatus().getMessage()).isEqualTo(MISSING_REQUIRED_LABEL);
    assertThat(response.getResponse().getAllowed()).isFalse();
  }

  @Test
  public void mutatesResource() {
    AdmissionController<HasMetadata> admissionController =
        new AdmissionController<>((resource, operation) -> {
          resource.getMetadata().getLabels().putIfAbsent(LABEL_KEY, LABEL_TEST_VALUE);
          return resource;
        });
    var inputAdmissionReview = createTestAdmissionReview();

    var response = admissionController.handle(inputAdmissionReview);

    assertThat(response.getResponse().getAllowed()).isTrue();
    String patch = new String(Base64.getDecoder().decode(response.getResponse().getPatch()));
    assertThat(patch)
        .isEqualTo(
            "[{\"op\":\"add\",\"path\":\"/metadata/labels/app.kubernetes.io~1name\",\"value\":\"mutation-test\"}]");
  }

  private AdmissionReview createTestAdmissionReview() {
    AdmissionReview admissionReview = new AdmissionReview();
    AdmissionRequest request = new AdmissionRequest();
    admissionReview.setRequest(request);
    request.setOperation(Operation.CREATE.name());
    request.setUid(UUID.randomUUID().toString());
    Deployment deployment = null;
    try (InputStream is = getClass().getResourceAsStream("deployment.yaml")) {
      deployment = Serialization.unmarshal(is, Deployment.class);
      request.setObject(deployment);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return admissionReview;
  }

}
