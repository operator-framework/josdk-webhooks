package io.javaoperatorsdk.webhook.sample;

import java.io.*;
import java.net.URL;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.BeforeAll;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.javaoperatorsdk.webhook.sample.commons.Utils;

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

  private static void applyAndWait(KubernetesClient client, InputStream is) {
    var resources = client.load(is).get();
    Utils.applyAndWait(client, resources, null);
  }

  private static void applyAndWait(KubernetesClient client, String file) {
    applyAndWait(client, file, null);
  }

  private static void applyAndWait(KubernetesClient client, String file,
      UnaryOperator<HasMetadata> transformer) {
    try (var fis = new FileInputStream(file)) {
      var resources = client.load(fis).get();
      Utils.applyAndWait(client, resources, transformer);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
