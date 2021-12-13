package io.javaoperatorsdk.admissioncontroller.mutation;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionRequest;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionResponse;
import io.javaoperatorsdk.admissioncontroller.AdmissionUtils;
import io.javaoperatorsdk.admissioncontroller.NotAllowedException;
import io.javaoperatorsdk.admissioncontroller.Operation;
import io.javaoperatorsdk.admissioncontroller.RequestHandler;
import io.javaoperatorsdk.admissioncontroller.clone.Cloner;
import io.javaoperatorsdk.admissioncontroller.clone.ObjectMapperCloner;

import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.admissionResponseFromMutation;
import static io.javaoperatorsdk.admissioncontroller.AdmissionUtils.getTargetResource;

public class DefaultRequestMutator<T extends KubernetesResource> implements RequestHandler {

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
      admissionResponse = admissionResponseFromMutation(originalResource, mutatedResource);
    } catch (NotAllowedException e) {
      admissionResponse = AdmissionUtils.notAllowedExceptionToAdmissionResponse(e);
    }
    return admissionResponse;
  }

}
