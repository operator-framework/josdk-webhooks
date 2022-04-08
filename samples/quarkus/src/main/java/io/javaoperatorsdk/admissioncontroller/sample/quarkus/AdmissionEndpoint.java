package io.javaoperatorsdk.admissioncontroller.sample.quarkus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import io.javaoperatorsdk.admissioncontroller.AsyncAdmissionController;
import io.smallrye.mutiny.Uni;

import static io.javaoperatorsdk.admissioncontroller.sample.quarkus.AdmissionControllerConfig.*;

@Path("/")
public class AdmissionEndpoint {

  public static final String MUTATE_PATH = "mutate";
  public static final String VALIDATE_PATH = "validate";
  public static final String ASYNC_MUTATE_PATH = "async-mutate";
  public static final String ASYNC_VALIDATE_PATH = "async-validate";

  private AdmissionController<Pod> mutationController;
  private AdmissionController<Pod> validationController;

  private AsyncAdmissionController<Pod> asyncMutationController;
  private AsyncAdmissionController<Pod> asyncValidationController;

  @Inject
  public AdmissionEndpoint(
      @Named(MUTATING_CONTROLLER) AdmissionController<Pod> mutationController,
      @Named(VALIDATING_CONTROLLER) AdmissionController<Pod> validationController,
      @Named(ASYNC_MUTATING_CONTROLLER) AsyncAdmissionController<Pod> asyncMutationController,
      @Named(ASYNC_VALIDATING_CONTROLLER) AsyncAdmissionController<Pod> asyncValidationController) {
    this.mutationController = mutationController;
    this.validationController = validationController;
    this.asyncMutationController = asyncMutationController;
    this.asyncValidationController = asyncValidationController;
  }

  @POST
  @Path(MUTATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AdmissionReview mutate(AdmissionReview admissionReview) {
    return mutationController.handle(admissionReview);
  }

  @POST
  @Path(VALIDATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AdmissionReview validate(AdmissionReview admissionReview) {
    return validationController.handle(admissionReview);
  }

  @POST
  @Path(ASYNC_MUTATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<AdmissionReview> asyncMutate(AdmissionReview admissionReview) {
    return Uni.createFrom().completionStage(this.asyncMutationController.handle(admissionReview));
  }

  @POST
  @Path(ASYNC_VALIDATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<AdmissionReview> asyncValidate(AdmissionReview admissionReview) {
    return Uni.createFrom().completionStage(this.asyncValidationController.handle(admissionReview));
  }

}
