package io.javaoperatorsdk.webhook.admission;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.fabric8.zjsonpatch.JsonDiff;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class AdmissionUtils {

  public static final String JSON_PATCH = "JSONPatch";
  // Jackson 2 defaults (as used by the fabric8 client) keep the patch in the same wire format,
  // e.g. enums are written by name instead of toString().
  private static final ObjectMapper mapper = JsonMapper.builderWithJackson2Defaults().build();

  private AdmissionUtils() {}

  public static AdmissionResponse allowedAdmissionResponse() {
    var admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(true);
    return admissionResponse;
  }

  public static AdmissionResponse notAllowedExceptionToAdmissionResponse(
      NotAllowedException notAllowedException) {
    var admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(false);
    admissionResponse.setStatus(notAllowedException.getStatus());
    return admissionResponse;
  }

  public static KubernetesResource getTargetResource(AdmissionRequest admissionRequest,
      Operation operation) {
    return (KubernetesResource) (operation == Operation.DELETE ? admissionRequest.getOldObject()
        : admissionRequest.getObject());
  }

  public static AdmissionResponse admissionResponseFromMutation(KubernetesResource originalResource,
      KubernetesResource mutatedResource) {
    var admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(true);
    admissionResponse.setPatchType(JSON_PATCH);
    var originalResNode = mapper.valueToTree(originalResource);
    var mutatedResNode = mapper.valueToTree(mutatedResource);

    var diff = JsonDiff.asJson(originalResNode, mutatedResNode);
    var base64Diff =
        Base64.getEncoder().encodeToString(diff.toString().getBytes(StandardCharsets.UTF_8));
    admissionResponse.setPatch(base64Diff);
    return admissionResponse;
  }
}
