package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.webhook.admission.AdmissionController;
import io.javaoperatorsdk.webhook.admission.AsyncAdmissionController;
import io.smallrye.mutiny.Uni;

@Path("/")
public class AdmissionAdditionalTestEndpoint {

  public static final String ERROR_ASYNC_MUTATE_PATH = "error-async-mutate";
  public static final String ERROR_ASYNC_VALIDATE_PATH = "error-async-validate";
  public static final String ERROR_MUTATE_PATH = "error-mutate";
  public static final String ERROR_VALIDATE_PATH = "error-validate";

  private final AdmissionController<Pod> errorMutationController;
  private final AdmissionController<Pod> errorValidationController;
  private final AsyncAdmissionController<Pod> errorAsyncMutationController;
  private final AsyncAdmissionController<Pod> errorAsyncValidationController;

  @Inject
  public AdmissionAdditionalTestEndpoint(
      @Named(AdditionalAdmissionConfig.ERROR_MUTATING_CONTROLLER) AdmissionController<Pod> errorMutationController,
      @Named(AdditionalAdmissionConfig.ERROR_VALIDATING_CONTROLLER) AdmissionController<Pod> errorValidationController,
      @Named(AdditionalAdmissionConfig.ERROR_ASYNC_MUTATING_CONTROLLER) AsyncAdmissionController<Pod> errorAsyncMutationController,
      @Named(AdditionalAdmissionConfig.ERROR_ASYNC_VALIDATING_CONTROLLER) AsyncAdmissionController<Pod> errorAsyncValidationController) {
    this.errorMutationController = errorMutationController;
    this.errorValidationController = errorValidationController;
    this.errorAsyncMutationController = errorAsyncMutationController;
    this.errorAsyncValidationController = errorAsyncValidationController;
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
