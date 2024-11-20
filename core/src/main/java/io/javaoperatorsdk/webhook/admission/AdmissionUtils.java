package io.javaoperatorsdk.webhook.admission;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.fabric8.zjsonpatch.JsonDiff;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AdmissionUtils {

  public static final String JSON_PATCH = "JSONPatch";
  private static final ObjectMapper mapper = new ObjectMapper();

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
    return operation == Operation.DELETE ? admissionRequest.getOldObject()
        : admissionRequest.getObject();
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
