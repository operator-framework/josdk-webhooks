# kubernetes-webhooks-framework

Framework and tooling to support
implementing [admission controllers](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/)
and [conversion hooks](https://kubernetes.io/docs/tasks/extend-kubernetes/custom-resources/custom-resource-definition-versioning/#webhook-conversion)
for Kubernetes in Java. Supports both **quarkus** and **spring boot**. Both Sync and Async programing model.

## Sample Usage

### Admission Controllers

Defining a mutation or validation controller is simple as:

https://github.com/java-operator-sdk/kubernetes-webhooks-framework/blob/0946595d941b789caef6a69b34c2e5be8c6b59cf/samples/quarkus/src/main/java/io/javaoperatorsdk/admissioncontroller/sample/quarkus/AdmissionControllerConfig.java#L31-L68

What can be then simple used in an endpoint:

https://github.com/java-operator-sdk/kubernetes-webhooks-framework/blob/0946595d941b789caef6a69b34c2e5be8c6b59cf/samples/quarkus/src/main/java/io/javaoperatorsdk/admissioncontroller/sample/quarkus/AdmissionEndpoint.java#L57-L89

See samples also for details.

### Conversion Hooks

Conversion hooks follows the same patter described
in [Kuberbuilder](https://book.kubebuilder.io/multiversion-tutorial/conversion-concepts.html), thus first converts the
custom resource from actual version to a hub, and as next step from the hub to the target resource version.

To create the controller
register [mappers](https://github.com/java-operator-sdk/kubernetes-webhooks-framework/blob/main/core/src/main/java/io/javaoperatorsdk/webhook/conversion/Mapper.java)
:

https://github.com/java-operator-sdk/kubernetes-webhooks-framework/blob/2a2bce54b49ea3398bef95a9102ee8645e11cf87/samples/quarkus/src/main/java/io/javaoperatorsdk/webhook/admission/sample/quarkus/conversion/ConversionControllerConfig.java#L15-L29

and use the controllers in the endpoint:

https://github.com/java-operator-sdk/kubernetes-webhooks-framework/blob/2a2bce54b49ea3398bef95a9102ee8645e11cf87/samples/spring-boot/src/main/java/io/javaoperatorsdk/webhook/sample/springboot/conversion/ConversionEndpoint.java#L29-L40
