package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.networking.v1.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@Disabled
class AdmissionControllerE2E {

  private KubernetesClient client = new KubernetesClientBuilder().build();

  @BeforeAll
  static void deployService() throws IOException {
    try (KubernetesClient client = new KubernetesClientBuilder().build();
        InputStream certManager =
            new URL(
                "https://github.com/cert-manager/cert-manager/releases/download/v1.10.1/cert-manager.yaml")
                    .openStream()) {
      applyAndWait(client, certManager);
      applyAndWait(client, "target/kubernetes/kubernetes.yml");
      applyAndWait(client, "k8s/mutating-webhook-configuration.yml");
      applyAndWait(client, "k8s/mutating-webhook-configuration.yml");
    }
  }

  @Test
  void testValidationHook() {
    var res = client.network().v1().ingresses().resource(ingress("validate-test")).create();
  }

  private Ingress ingress(String name) {
    return new IngressBuilder()
        .withNewMetadata()
        .withName(name)
        .endMetadata()
        .withSpec(new IngressSpecBuilder()
            .withIngressClassName("sample")
            .withRules(new IngressRuleBuilder()
                .withHttp(new HTTPIngressRuleValueBuilder()
                    .withPaths(new HTTPIngressPathBuilder()
                        .withPath("/test")
                        .withBackend(new IngressBackendBuilder()
                            .withService(new IngressServiceBackendBuilder()
                                .withName("service")
                                .withPort(new ServiceBackendPortBuilder()
                                    .withNumber(80)
                                    .build())
                                .build())
                            .build())
                        .build())
                    .build())
                .build())
            .build())
        .build();
  }

  public static void applyAndWait(KubernetesClient client, String path) {
    try (FileInputStream fileInputStream = new FileInputStream(path)) {
      applyAndWait(client, fileInputStream);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void applyAndWait(KubernetesClient client, InputStream is) {
    var resources = client.load(is).get();
    client.resourceList(resources).createOrReplace();
    client.resourceList(resources).waitUntilReady(2, TimeUnit.MINUTES);
  }

}
