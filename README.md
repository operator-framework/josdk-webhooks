# admission-controller-framework

Framework and tooling to support implementing admission controllers for Kubernetes in Java

Currently, project is early phases.


## Sample Usage

Defining a mutation or validation controller should be simple as: 

```java

@Dependent
public class AdmissionControllerConfig {

    public static final String APP_NAME_LABEL_KEY = "app.kubernetes.io/name";

    @Singleton
    @Named("mutatingController")
    public AdmissionController<Pod> mutatingController() {
        return new AdmissionController<>((resource, operation) -> {
            resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
            return resource;
        });
    }
    
    @Singleton
    @Named("validatingController")
    public AdmissionController<Pod> validatingController() {
        return new AdmissionController<>((resource, operation) -> {
            if (resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
                throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
            }
        });
    }
}

```

What can be then simple used in an endpoint:

```java

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

```


See samples also for details.
