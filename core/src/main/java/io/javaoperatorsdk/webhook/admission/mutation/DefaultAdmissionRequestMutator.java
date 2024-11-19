package io.javaoperatorsdk.webhook.admission.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.webhook.admission.AdmissionRequestHandler;
import io.javaoperatorsdk.webhook.admission.AdmissionUtils;
import io.javaoperatorsdk.webhook.admission.NotAllowedException;
import io.javaoperatorsdk.webhook.admission.Operation;
import io.javaoperatorsdk.webhook.clone.Cloner;
import io.javaoperatorsdk.webhook.clone.ObjectMapperCloner;

import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.admissionResponseFromMutation;
import static io.javaoperatorsdk.webhook.admission.AdmissionUtils.getTargetResource;

public class DefaultAdmissionRequestMutator<T extends KubernetesResource>
    implements AdmissionRequestHandler {

  private final Mutator<T> mutator;
  private final Cloner<T> cloner;

  public DefaultAdmissionRequestMutator(Mutator<T> mutator) {
    this(mutator, new ObjectMapperCloner<>());
  }

  public DefaultAdmissionRequestMutator(Mutator<T> mutator, Cloner<T> cloner) {
    this.mutator = mutator;
    this.cloner = cloner;
  }

  @Override
  @SuppressWarnings("unchecked")
  public AdmissionResponse handle(AdmissionRequest admissionRequest) {
    var operation = Operation.valueOf(admissionRequest.getOperation());
    var originalResource = (T) getTargetResource(admissionRequest, operation);
    var clonedResource = cloner.clone(originalResource);
    AdmissionResponse admissionResponse;
    try {
      var mutatedResource = mutator.mutate(clonedResource, operation);
      admissionResponse = admissionResponseFromMutation(originalResource, mutatedResource);
    } catch (NotAllowedException e) {
      admissionResponse = AdmissionUtils.notAllowedExceptionToAdmissionResponse(e);
    }
    return admissionResponse;
  }
}
