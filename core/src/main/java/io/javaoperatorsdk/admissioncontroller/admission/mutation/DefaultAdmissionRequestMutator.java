package io.javaoperatorsdk.admissioncontroller.admission.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.admission.AdmissionRequestHandler;
import io.javaoperatorsdk.admissioncontroller.admission.AdmissionUtils;
import io.javaoperatorsdk.admissioncontroller.admission.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.admission.Operation;
import io.javaoperatorsdk.admissioncontroller.clone.Cloner;
import io.javaoperatorsdk.admissioncontroller.clone.ObjectMapperCloner;

import static io.javaoperatorsdk.admissioncontroller.admission.AdmissionUtils.admissionResponseFromMutation;
import static io.javaoperatorsdk.admissioncontroller.admission.AdmissionUtils.getTargetResource;

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
  public AdmissionResponse handle(AdmissionRequest admissionRequest) {
    Operation operation = Operation.valueOf(admissionRequest.getOperation());
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
