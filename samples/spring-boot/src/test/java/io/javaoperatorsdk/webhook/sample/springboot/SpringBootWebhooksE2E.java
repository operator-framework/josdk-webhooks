package io.javaoperatorsdk.webhook.sample.springboot;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.javaoperatorsdk.webhook.sample.AbstractEndToEndTest;

import static io.javaoperatorsdk.webhook.sample.commons.Utils.addConversionHookEndpointToCustomResource;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.applyAndWait;

class SpringBootWebhooksE2E extends AbstractEndToEndTest {

  @BeforeAll
  static void deployService() throws IOException {
    try (var client = new KubernetesClientBuilder().withConfig(new ConfigBuilder()
        .withNamespace("default")
        .build()).build();
        var certManager = new URL(
            "https://github.com/cert-manager/cert-manager/releases/download/v1.10.1/cert-manager.yaml")
            .openStream()) {
      applyAndWait(client, certManager);
      applyAndWait(client, "k8s/kubernetes.yml");
      applyAndWait(client, "k8s/validating-webhook-configuration.yml");
      applyAndWait(client, "k8s/mutating-webhook-configuration.yml");
      applyAndWait(client,
          "../commons/target/classes/META-INF/fabric8/multiversioncustomresources.sample.javaoperatorsdk-v1.yml",
          addConversionHookEndpointToCustomResource("spring-boot-sample"));
    }
  }
}
