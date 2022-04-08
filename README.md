# admission-controller-framework

Framework and tooling to support implementing admission controllers for Kubernetes in Java.
Supports both **quarkus** and **spring boot**. Both Sync and Async programing model.

## Sample Usage

Defining a mutation or validation controller should be simple as: 

https://github.com/java-operator-sdk/admission-controller-framework/blob/0946595d941b789caef6a69b34c2e5be8c6b59cf/samples/quarkus/src/main/java/io/javaoperatorsdk/admissioncontroller/sample/quarkus/AdmissionControllerConfig.java#L31-L68

What can be then simple used in an endpoint:

https://github.com/java-operator-sdk/admission-controller-framework/blob/0946595d941b789caef6a69b34c2e5be8c6b59cf/samples/quarkus/src/main/java/io/javaoperatorsdk/admissioncontroller/sample/quarkus/AdmissionEndpoint.java#L57-L89

See samples also for details.
