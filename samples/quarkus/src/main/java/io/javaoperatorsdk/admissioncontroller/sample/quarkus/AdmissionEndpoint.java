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
  public static final String ERROR_MUTATE_PATH = "error-mutate";
  public static final String ERROR_VALIDATE_PATH = "error-validate";
  public static final String ASYNC_MUTATE_PATH = "async-mutate";
  public static final String ASYNC_VALIDATE_PATH = "async-validate";
  public static final String ERROR_ASYNC_MUTATE_PATH = "error-async-mutate";
  public static final String ERROR_ASYNC_VALIDATE_PATH = "error-async-validate";

  private final AdmissionController<Pod> mutationController;
  private final AdmissionController<Pod> validationController;
  private final AdmissionController<Pod> errorMutationController;
  private final AdmissionController<Pod> errorValidationController;
  private final AsyncAdmissionController<Pod> asyncMutationController;
  private final AsyncAdmissionController<Pod> asyncValidationController;
  private final AsyncAdmissionController<Pod> errorAsyncMutationController;
  private final AsyncAdmissionController<Pod> errorAsyncValidationController;

  @Inject
  public AdmissionEndpoint(
      @Named(MUTATING_CONTROLLER) AdmissionController<Pod> mutationController,
      @Named(VALIDATING_CONTROLLER) AdmissionController<Pod> validationController,
      @Named(ERROR_MUTATING_CONTROLLER) AdmissionController<Pod> errorMutationController,
      @Named(ERROR_VALIDATING_CONTROLLER) AdmissionController<Pod> errorValidationController,
      @Named(ASYNC_MUTATING_CONTROLLER) AsyncAdmissionController<Pod> asyncMutationController,
      @Named(ASYNC_VALIDATING_CONTROLLER) AsyncAdmissionController<Pod> asyncValidationController,
      @Named(ERROR_ASYNC_MUTATING_CONTROLLER) AsyncAdmissionController<Pod> errorAsyncMutationController,
      @Named(ERROR_ASYNC_VALIDATING_CONTROLLER) AsyncAdmissionController<Pod> errorAsyncValidationController) {
    this.mutationController = mutationController;
    this.validationController = validationController;
    this.errorMutationController = errorMutationController;
    this.errorValidationController = errorValidationController;
    this.asyncMutationController = asyncMutationController;
    this.asyncValidationController = asyncValidationController;
    this.errorAsyncMutationController = errorAsyncMutationController;
    this.errorAsyncValidationController = errorAsyncValidationController;
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

  @POST
  @Path(ERROR_ASYNC_MUTATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<AdmissionReview> errorAsyncMutate(AdmissionReview admissionReview) {
    return Uni.createFrom()
        .completionStage(() -> this.errorAsyncMutationController.handle(admissionReview));
  }

  @POST
  @Path(ERROR_ASYNC_VALIDATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<AdmissionReview> errorAsyncValidate(AdmissionReview admissionReview) {
    return Uni.createFrom()
        .completionStage(() -> this.errorAsyncValidationController.handle(admissionReview));
  }

  @POST
  @Path(ERROR_MUTATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AdmissionReview errorMutate(AdmissionReview admissionReview) {
    return errorMutationController.handle(admissionReview);
  }

  @POST
  @Path(ERROR_VALIDATE_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AdmissionReview errorValidate(AdmissionReview admissionReview) {
    return errorValidationController.handle(admissionReview);
  }

}
