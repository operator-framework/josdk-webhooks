package io.javaoperatorsdk.webhook.admission;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;

import static org.assertj.core.api.Assertions.assertThat;

public class Commons {

  public static final String LABEL_KEY = "app.kubernetes.io/name";
  public static final String MISSING_REQUIRED_LABEL = "Missing required label.";
  public static final String LABEL_TEST_VALUE = "mutation-test";

  public static AdmissionReview createTestAdmissionReview() {
    AdmissionReview admissionReview = new AdmissionReview();
    AdmissionRequest request = new AdmissionRequest();
    admissionReview.setRequest(request);
    request.setOperation(Operation.CREATE.name());
    request.setUid(UUID.randomUUID().toString());
    Deployment deployment = null;
    try (InputStream is = Commons.class.getResourceAsStream("deployment.yaml")) {
      deployment = Serialization.unmarshal(is, Deployment.class);
      request.setObject(deployment);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return admissionReview;
  }

  public static void assertMutation(AdmissionReview admissionReview) {
    assertThat(admissionReview.getResponse().getAllowed()).isTrue();
    String patch = new String(Base64.getDecoder().decode(admissionReview.getResponse().getPatch()));
    assertThat(patch)
        .isEqualTo(
            "[{\"op\":\"add\",\"path\":\"/metadata/labels/app.kubernetes.io~1name\",\"value\":\"mutation-test\"}]");
  }

  public static void assertValidation(AdmissionReview admissionReview, String requestUid) {
    assertThat(admissionReview.getResponse().getUid())
        .isEqualTo(requestUid);
    assertThat(admissionReview.getResponse().getStatus().getCode()).isEqualTo(403);
    assertThat(admissionReview.getResponse().getStatus().getMessage())
        .isEqualTo(MISSING_REQUIRED_LABEL);
    assertThat(admissionReview.getResponse().getAllowed()).isFalse();
  }

}
