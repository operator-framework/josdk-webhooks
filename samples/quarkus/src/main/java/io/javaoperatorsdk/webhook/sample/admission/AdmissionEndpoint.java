package io.javaoperatorsdk.webhook.sample.admission;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.smallrye.mutiny.Uni;

@Path("/")
public class AdmissionEndpoint {

  public static final String MUTATE_PATH = "mutate";
  public static final String VALIDATE_PATH = "validate";
  public static final String ASYNC_MUTATE_PATH = "async-mutate";
  public static final String ASYNC_VALIDATE_PATH = "async-validate";

  private final AdmissionController<Ingress> mutationController;
  private final AdmissionController<Ingress> validationController;
  private final AsyncAdmissionController<Ingress> asyncMutationController;
  private final AsyncAdmissionController<Ingress> asyncValidationController;

  @Inject
  public AdmissionEndpoint(
      @Named(AdmissionControllerConfig.MUTATING_CONTROLLER) AdmissionController<Ingress> mutationController,
      @Named(AdmissionControllerConfig.VALIDATING_CONTROLLER) AdmissionController<Ingress> validationController,
      @Named(AdmissionControllerConfig.ASYNC_MUTATING_CONTROLLER) AsyncAdmissionController<Ingress> asyncMutationController,
      @Named(AdmissionControllerConfig.ASYNC_VALIDATING_CONTROLLER) AsyncAdmissionController<Ingress> asyncValidationController) {
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
