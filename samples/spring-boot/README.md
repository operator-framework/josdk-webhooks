## Admission Control for Pod resources in Spring Boot

The idea of this example is to demonstrate how we can alter resources and, then, validate them using [Admission Control webhooks](https://kubernetes.io/docs/reference/access-authn-authz/extensible-admission-controllers) webhooks in Spring Boot. 

To be precise, this example will install two admission webhooks that watch and manage Pod resources:
(1) a Mutating admission webhook that will add the label "app.kubernetes.io/name" with value "mutation-test" to the Pod resource
(2) a Validation admission webhook that will verify all the new Pod resources have the label "app.kubernetes.io/name"

**Note:** Kubernetes will first invoke mutating webhooks and then will invoke validation webhooks at this order.

### Introduction

The admission controllers are installed via either [the ValidatingWebhookConfiguration](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.24/#validatingwebhookconfiguration-v1-admissionregistration-k8s-io) resource for validation admission webhooks or [the MutatingWebhooksConfiguration](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.24/#mutatingwebhookconfiguration-v1-admissionregistration-k8s-io) resource for mutating admission webhooks. For example, the ValidatingWebhookConfiguration resource looks like:

```yaml
apiVersion: admissionregistration.k8s.io/v1
kind: ValidatingWebhookConfiguration
metadata:
  name: "my-validation-webhook"
webhooks:
  - name: "my-validation-webhook"
    rules: ## 1
      - apiGroups:   [""]
        apiVersions: ["v1"]
        operations:  ["CREATE"]
        resources:   ["pods"]
        scope:       "Namespaced"
    clientConfig:
      service:
        namespace: "test"
        name: "spring-boot-sample" ## 2
        path: "/validate"
      caBundle: "<CA_BUNDLE>" ## 3
    admissionReviewVersions: ["v1"]
```

The above admission webhook configuration is going to watch "CREATE" events of POD resources (see ## 1). When new POD resources are installed into our Kubernetes cluster, then Kubernetes will invoke the POST resource with path "/validate" from the service name "spring-boot-sample" and Kubernetes namespace "test" (see ## 2). The complete URL that Kubernetes will do is "https://spring-boot-sample.test.svc:443/validate", so our application needs to be configured with SSL enabled and the webhook needs to be provided with the right CA bundle (see ## 3) that is accepted by the application.

### Requirements

Before getting started, you need to:

1. have connected to your Kubernetes cluster using `kubectl/oc`.
2. have installed [the Cert Manager operator](https://cert-manager.io/) into your Kubernetes cluster.
3. have logged into a container registry such as `quay.io` at your choice.

### Getting Started

The first thing we need is to add the [Dekorate Spring Boot](https://dekorate.io/docs/spring-boot-integration) extension to generate the `Deployment` and `Service` Kubernetes resources into our Maven pom file:

```xml
<dependency>
  <groupId>io.dekorate</groupId>
  <artifactId>kubernetes-spring-starter</artifactId>
</dependency>
```

The second thing we need is to enable SSL in Spring Boot. In this example, we're going to use the [Dekorate Cert-Manager](https://dekorate.io/docs/cert-manager) extension to generate the Cert-Manager resources and map the volumes where the certifications will be installed. See more information in [the Spring Boot with Cert-Manager example](https://github.com/dekorateio/dekorate/tree/main/examples/spring-boot-with-certmanager-example#enable-https-transport) from Dekorate. So, let's add this extension to our Maven pom file as well:

```xml
<dependency>
  <groupId>io.dekorate</groupId>
  <artifactId>certmanager-annotations</artifactId>
</dependency>
```

Finally, update the application properties as suggested in [the Spring Boot with Cert-Manager example](https://github.com/dekorateio/dekorate/tree/main/examples/spring-boot-with-certmanager-example#enable-https-transport).

### Deployment in Kubernetes

Let's start by creating the Kubernetes namespace `test` where we'll install all the resources:

```
kubectl create namespace test
kubectl config set-context --current --namespace=test
```

Next, let's generate all the manifests and push the container image into your container registry. This example uses [Dekorate](https://dekorate.io/) to generate all the Kubernetes resources, so we can generate the manifests and build/push the image at once simply by running the following Maven command:

```
mvn clean install \
  -Ddekorate.docker.registry=<CONTAINER REGISTRY URL> \
  -Ddekorate.docker.group=<CONTAINER REGISTRY USER> \
  -Ddekorate.docker.version=<VERSION> \
  -Ddekorate.docker.autoPushEnabled=true
```

For example, if our container registry url is `quay.io`, the group is `jcarvaja` and the version is `latest`; the previous Maven command will build and push the image `quay.io/jcarvaja/spring-boot-sample:latest`. 

Moreover, the above command will also generate the following resources in the `target/classes/META-INF/kubernetes.yaml` file:
- `Deployment` and `Service` resources
- `Certificate` and `Issuer` resources to configure the Cert-Manager operator

So, let's install all the generated resources into our Kubernetes cluster:

```
kubectl apply -f target/classes/META-INF/dekorate/kubernetes.yml
```

When installed, the Cert-Manager operator will watch the `Certificate` and `Issuer` resources and will generate a secret named `tls-secret`. We can see the content by running the following command:

```
kubectl -n test get secret tls-secret -o yaml
```

Next, we need to configure and install the admission webhook with the proper CA certificate (key `ca.crt` from the secret). Luckily, we can leverage this configuration to the Cert-Manager operator by using the annotation `cert-manager.io/inject-ca-from` (more information in [here](https://cert-manager.io/docs/concepts/ca-injector/#injecting-ca-data-from-a-certificate-resource)). 

Let's install the admission webhook with the annotation `cert-manager.io/inject-ca-from=test/spring-boot-sample`:

```
kubectl apply -f k8s/validating-webhook-configuration.yml
```

And let's verify that the `Cert-Manager` operator has populated the `caBundle` field thanks to the annotation `cert-manager.io/inject-ca-from`:

```
kubectl get validatingwebhookconfigurations.admissionregistration.k8s.io pod-policy.spring-boot.example.com -o yaml | grep caBundle
```

**Note:** The content of the caBundle should be the same as in `kubectl -n test get secret tls-secret -o yaml | grep ca.crt`.

So far, we have installed the validating webhook! Let's see it in action by creating a new POD resource:

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: pod-with-missing-label
spec:
  containers:
    - image: any
      imagePullPolicy: IfNotPresent
      name: spring-boot-sample
```

This POD resource should not pass the validation as the label "app.kubernetes.io/name" does not exist.

When we install it, we should get the following error:

```
> kubectl apply -f k8s/create-pod-with-missing-label-example.yaml
Error from server: error when creating "k8s/create-pod-with-missing-label-example.yaml": admission webhook "pod-policy.spring-boot.example.com" denied the request: Missing label: app.kubernetes.io/name
```

So good so far!

Let's now install the mutating webhook:

```
kubectl apply -f k8s/mutating-webhook-configuration.yml
```

And again, let's install the same Pod without annotations:

```
> kubectl apply -f k8s/create-pod-with-missing-label-example.yaml
pod/pod-with-missing-label created
```

Now, the pod resource passed the validation because the mutate webhook added the missing label. We can see the installed Pod resource:

```
> kubectl get pod pod-with-missing-label -o yaml | grep app.kubernetes.io/name
app.kubernetes.io/name: mutation-test
```

This label was added by our mutate webhook (see logic in [here](https://github.com/java-operator-sdk/admission-controller-framework/blob/ce64f6e2a1a11a538d73acf6c49af96c04ed484d/samples/spring-boot/src/main/java/io/javaoperatorsdk/webhook/sample/springboot/Config.java#L57)).