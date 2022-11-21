package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.smallrye.mutiny.Uni;

@Path("/")
public class AdmissionEndpoint {

  public static final String MUTATE_PATH = "mutate";
  public static final String VALIDATE_PATH = "validate";
  public static final String ASYNC_MUTATE_PATH = "async-mutate";
  public static final String ASYNC_VALIDATE_PATH = "async-validate";

  private final AdmissionController<Pod> mutationController;
  private final AdmissionController<Pod> validationController;
  private final AsyncAdmissionController<Pod> asyncMutationController;
  private final AsyncAdmissionController<Pod> asyncValidationController;

  @Inject
  public AdmissionEndpoint(
      @Named(AdmissionControllerConfig.MUTATING_CONTROLLER) AdmissionController<Pod> mutationController,
      @Named(AdmissionControllerConfig.VALIDATING_CONTROLLER) AdmissionController<Pod> validationController,
      @Named(AdmissionControllerConfig.ASYNC_MUTATING_CONTROLLER) AsyncAdmissionController<Pod> asyncMutationController,
      @Named(AdmissionControllerConfig.ASYNC_VALIDATING_CONTROLLER) AsyncAdmissionController<Pod> asyncValidationController) {
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
    return Uni.createFrom()
        .completionStage(() -> this.asyncMutationController.handle(admissionReview));
  }

  @POST
  @Path(ASYNC_VALIDATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<AdmissionReview> asyncValidate(AdmissionReview admissionReview) {
    return Uni.createFrom()
        .completionStage(() -> this.asyncValidationController.handle(admissionReview));
  }
}
