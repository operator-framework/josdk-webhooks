package io.javaoperatorsdk.webhook.admission.sample.quarkus.admission;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@Disabled
class AdmissionControllerE2E {

  @BeforeAll
  static void deployService() throws IOException {
    try (KubernetesClient client = new KubernetesClientBuilder().build();
        FileInputStream fileInputStream = new FileInputStream("target/kubernetes.yml");
        InputStream certManager =
            new URL(
                "https://github.com/cert-manager/cert-manager/releases/download/v1.10.1/cert-manager.yaml")
                    .openStream();) {
      client.resourceList(client.load(fileInputStream).get()).createOrReplace();
    }
  }

  @Test
  void testValidationHook() {


  }


}
