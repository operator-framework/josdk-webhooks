# Kubernetes Webhooks Framework Documentation

## Intro

Kubernetes Webhooks Framework makes it simple to
implementing [admission controllers](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/)
and [conversion hooks](https://kubernetes.io/docs/tasks/extend-kubernetes/custom-resources/custom-resource-definition-versioning/#webhook-conversion)
for Kubernetes in Java.

## Getting Started

Before you start make sure you understand these concepts in Kubernetes, reading the docs mentioned above.

### Samples

There are samples both
for [Spring Boot](https://github.com/java-operator-sdk/admission-controller-framework/tree/main/samples/spring-boot)
and [Quarkus](https://github.com/java-operator-sdk/kubernetes-webooks-framework/tree/main/samples/quarkus), both of them
implements the same logic. Both sync and async APIs
are showcased. This documentation describes the Quarkus version, however Spring Boot version is almost identical.

There are two endpoint, one
for [admission controllers](https://github.com/java-operator-sdk/admission-controller-framework/blob/main/samples/quarkus/src/main/java/io/javaoperatorsdk/webhook/sample/admission/AdmissionEndpoint.java)
(a validating and a mutating) and one for the
sample [conversion hook](https://github.com/java-operator-sdk/admission-controller-framework/blob/76fd9c4b9fef6738310a7dd97b159c3668ced9f1/samples/quarkus/src/main/java/io/javaoperatorsdk/webhook/sample/conversion/ConversionEndpoint.java)
.

Starting from those endpoints, it should be trivial to understand how the underlying logic works.

### End-To-End Tests

The [end-to-end tests](https://github.com/java-operator-sdk/admission-controller-framework/blob/main/samples/quarkus/src/test/java/io/javaoperatorsdk/webhook/sample/QuarkusWebhooksE2E.java)
are based on the samples (See Spring Boot
version [here](https://github.com/java-operator-sdk/admission-controller-framework/blob/e2637a90152bebfca2983ba17268c1f7ec7e9602/samples/spring-boot/src/test/java/io/javaoperatorsdk/webhook/sample/springboot/SpringBootWebhooksE2E.java)).
To see how those tests are executed check
the [related GitHub Action](https://github.com/java-operator-sdk/admission-controller-framework/blob/main/.github/workflows/pr.yml#L66-L66)

The samples are first built, then deployed to a local Kubernetes cluster (in our case minikube is used).
For Quarkus most of the deployment artifact is generated using extensions (works in similar way for Spring Boot,
using [dekorate](https://github.com/java-operator-sdk/admission-controller-framework/blob/main/samples/spring-boot/pom.xml#L52-L63)):

```xml

<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-kubernetes</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkiverse.certmanager</groupId>
    <artifactId>quarkus-certmanager</artifactId>
</dependency>
```

Only additional resources used for admission hooks, are present in
the [k8s](https://github.com/java-operator-sdk/admission-controller-framework/tree/main/samples/quarkus/k8s)
directory. These are the configuration files to configure the admission hooks. For example the configuration for
validation:

```yaml

apiVersion: admissionregistration.k8s.io/v1
kind: ValidatingWebhookConfiguration
metadata:
  name: "validating.quarkus.example.com"
  annotations:
    cert-manager.io/inject-ca-from: default/quarkus-sample
webhooks:
  - name: "validating.quarkus.example.com"
    rules:
      - apiGroups: [ "networking.k8s.io" ]
        apiVersions: [ "v1" ]
        operations: [ "*" ]
        resources: [ "ingresses" ]
        scope: "Namespaced"
    clientConfig:
      service:
        namespace: "default"
        name: "quarkus-sample"
        path: "/validate"
        port: 443
    admissionReviewVersions: [ "v1" ]
    sideEffects: None
    timeoutSeconds: 5
```

The conversion hook is configured within the `CustomResourceDefinition`, see
related [Kubernetes docs](https://kubernetes.io/docs/tasks/extend-kubernetes/custom-resources/custom-resource-definition-versioning/#configure-customresourcedefinition-to-use-conversion-webhooks).
Since this is [not yet supported](https://github.com/fabric8io/kubernetes-client/issues/4692) by the fabric8 client CRD
generator, the hook definition is
[added before applied](https://github.com/java-operator-sdk/admission-controller-framework/blob/e2637a90152bebfca2983ba17268c1f7ec7e9602/samples/commons/src/test/java/io/javaoperatorsdk/webhook/sample/EndToEndTestBase.java#L97-L124).

Note
that [cert manager](https://github.com/java-operator-sdk/admission-controller-framework/blob/e2637a90152bebfca2983ba17268c1f7ec7e9602/samples/quarkus/src/test/java/io/javaoperatorsdk/webhook/sample/QuarkusWebhooksE2E.java#L19-L23)
is used to generate

## Admission Controllers

### API

### Deployments

## Conversion Hooks

### API

### Deployments



