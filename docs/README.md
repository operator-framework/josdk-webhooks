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
are based on the samples. To see how those tests are executed check the [related GitHub Action](https://github.com/java-operator-sdk/admission-controller-framework/blob/main/.github/workflows/pr.yml#L66-L66) 

The samples are first built, then are deployed to a local Kubernetes cluster (in our case minikube is used).
For Quarkus most of the deployment artifact is generated using extensions:




## Admission Controllers

### API

### Deployments

## Conversion Hooks

### API

### Deployments



