package io.javaoperatorsdk.admissioncontroller.mutation;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.fabric8.zjsonpatch.JsonDiff;
import io.javaoperatorsdk.admissioncontroller.AdmissionUtils;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;
import io.javaoperatorsdk.admissioncontroller.RequestHandler;
import io.javaoperatorsdk.admissioncontroller.clone.Cloner;
import io.javaoperatorsdk.admissioncontroller.clone.ObjectMapperCloner;

import com.fasterxml.jackson.databind.ObjectMapper;

import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.getTargetResource;

public class DefaultRequestMutator<T extends KubernetesResource> implements RequestHandler {

  public static final String JSON_PATCH = "JSONPatch";
  private final ObjectMapper mapper = new ObjectMapper();

  private final Mutator<T> mutator;
  private final Cloner<T> cloner;

  public DefaultRequestMutator(Mutator<T> mutator) {
    this(mutator, new ObjectMapperCloner<>());
  }

  public DefaultRequestMutator(Mutator<T> mutator, Cloner<T> cloner) {
    this.mutator = mutator;
    this.cloner = cloner;
  }

  @Override
  public AdmissionResponse handle(AdmissionRequest admissionRequest) {
    Operation operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);
    var clonedResource = cloner.clone(originalResource);
    AdmissionResponse admissionResponse;
    try {
      var mutatedResource = mutator.mutate(clonedResource, operation);
      admissionResponse = createAdmissionResponseFromMutation(originalResource, mutatedResource);
    } catch (NotAllowedException e) {
      admissionResponse = AdmissionUtils.notAllowedExceptionToAdmissionResponse(e);
    }
    return admissionResponse;
  }

  private AdmissionResponse createAdmissionResponseFromMutation(T originalResource,
      T mutatedResource) {
    AdmissionResponse admissionResponse = new AdmissionResponse();
    admissionResponse.setAllowed(true);
    admissionResponse.setPatchType(JSON_PATCH);
    var originalResNode = mapper.valueToTree(originalResource);
    var mutatedResNode = mapper.valueToTree(mutatedResource);

    var diff = JsonDiff.asJson(originalResNode, mutatedResNode);
    String base64Diff =
        Base64.getEncoder().encodeToString(diff.textValue().getBytes(StandardCharsets.UTF_8));
    admissionResponse.setPatch(base64Diff);
    return admissionResponse;
  }

}
