package io.javaoperatorsdk.admissioncontroller.sample.quarkus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;

@Path("/")
public class AdmissionEndpoint {

  private AdmissionController<Pod> mutationController;
  private AdmissionController<Pod> validationController;

  @Inject
  public AdmissionEndpoint(@Named("mutatingController") AdmissionController<Pod> mutationController,
      @Named("validatingController") AdmissionController<Pod> validationController) {
    this.mutationController = mutationController;
    this.validationController = validationController;
  }

  @POST
  @Path("mutate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AdmissionReview mutate(AdmissionReview admissionReview) {
    return mutationController.handle(admissionReview);
  }

  @POST
  @Path("validate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AdmissionReview hello(AdmissionReview admissionReview) {
    return validationController.handle(admissionReview);
  }

}
