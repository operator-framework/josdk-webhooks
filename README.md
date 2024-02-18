# kubernetes-webhooks-framework

Framework and tooling to support
implementing [dynamic admission controllers](https://kubernetes.io/docs/reference/access-authn-authz/extensible-admission-controllers/)
and [conversion hooks](https://kubernetes.io/docs/tasks/extend-kubernetes/custom-resources/custom-resource-definition-versioning/#webhook-conversion)
for Kubernetes in Java. Supports both **quarkus** and **spring boot**. Both **sync** and **async** programing models.

## Documentation

**For more detailed documentation check the [docs](docs).**

## Sample Usage

```xml
<dependency>
  <groupId>io.javaoperatorsdk</groupId>
  <artifactId>kubernetes-webhooks-framework-core</artifactId>
  <version>${josdk.webhooks.version}</version>
</dependency>
```

### Dynamic Admission Controllers

Defining a mutation or validation controller is as simple as:

```java

  @Singleton
  @Named(MUTATING_CONTROLLER)
  public AdmissionController<Ingress> mutatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null) {
        resource.getMetadata().setLabels(new HashMap<>());
      }
      resource.getMetadata().getLabels().putIfAbsent(APP_NAME_LABEL_KEY, "mutation-test");
      return resource;
    });
  }
  
  @Singleton
  @Named(VALIDATING_CONTROLLER)
  public AdmissionController<Ingress> validatingController() {
    return new AdmissionController<>((resource, operation) -> {
      if (resource.getMetadata().getLabels() == null
              || resource.getMetadata().getLabels().get(APP_NAME_LABEL_KEY) == null) {
        throw new NotAllowedException("Missing label: " + APP_NAME_LABEL_KEY);
      }
    });
  }

```

What can be simply used in an endpoint:

```java
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
```


See samples also for details.

### Conversion Hooks

Conversion hooks follows the same patter described
in [Kuberbuilder](https://book.kubebuilder.io/multiversion-tutorial/conversion-concepts.html), thus first converts the
custom resource from actual version to a hub, and as next step from the hub to the target resource version.

To create the controller
register [mappers](https://github.com/java-operator-sdk/kubernetes-webhooks-framework/blob/main/core/src/main/java/io/javaoperatorsdk/webhook/conversion/Mapper.java)
:

```java
  @Singleton
  public ConversionController conversionController() {
    var controller = new ConversionController();
    controller.registerMapper(new V1Mapper());
    controller.registerMapper(new V2Mapper());
    return controller;
  }
```

and use the controllers in the endpoint:

```java
  @PostMapping(CONVERSION_PATH)
  @ResponseBody
  public ConversionReview convert(@RequestBody ConversionReview conversionReview) {
    return conversionController.handle(conversionReview);
  }
```
