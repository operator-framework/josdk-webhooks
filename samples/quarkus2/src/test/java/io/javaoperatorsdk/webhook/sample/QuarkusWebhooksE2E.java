package io.javaoperatorsdk.webhook.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import static io.javaoperatorsdk.webhook.sample.commons.Utils.addConversionHookEndpointToCustomResource;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.applyAndWait;

class QuarkusWebhooksE2E extends EndToEndTestBase {

  @BeforeAll
  static void deployService() throws IOException {
    try (KubernetesClient client = new KubernetesClientBuilder().build();
        InputStream certManager =
            new URL(
                "https://github.com/cert-manager/cert-manager/releases/download/v1.10.1/cert-manager.yaml")
                .openStream()) {
      applyAndWait(client, certManager);
      applyAndWait(client, "target/kubernetes/minikube.yml");
      applyAndWait(client, "k8s/validating-webhook-configuration.yml");
      applyAndWait(client, "k8s/mutating-webhook-configuration.yml");
      applyAndWait(client,
          "../commons/target/classes/META-INF/fabric8/multiversioncustomresources.sample.javaoperatorsdk-v1.yml",
          addConversionHookEndpointToCustomResource("quarkus-sample"));
      waitForCoreDNS(client);
    }
  }
}
