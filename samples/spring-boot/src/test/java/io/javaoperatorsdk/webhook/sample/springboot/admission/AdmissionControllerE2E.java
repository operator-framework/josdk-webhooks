package io.javaoperatorsdk.webhook.sample.springboot.admission;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.networking.v1.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;

import static io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers.MUTATION_TARGET_LABEL;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
      applyAndWait(client, "target/classes/META-INF/dekorate/kubernetes.yml");
      applyAndWait(client, "k8s/validating-webhook-configuration.yml");
      applyAndWait(client, "k8s/mutating-webhook-configuration.yml");
    }
  }

  @Test
  void validationHook() {
    var ingressWithLabel = testIngress("normal-add-test");
    addRequiredLabels(ingressWithLabel);
    var res = client.network().v1().ingresses().resource(ingressWithLabel).createOrReplace();
    assertThat(res).isNotNull();

    assertThrows(KubernetesClientException.class,
        () -> client.network().v1().ingresses().resource(testIngress("validate-test"))
            .createOrReplace());
  }

  @Test
  void mutationHook() {
    var ingressWithLabel = testIngress("mutation-test");
    addRequiredLabels(ingressWithLabel);

    var res = client.network().v1().ingresses().resource(ingressWithLabel).createOrReplace();

    assertThat(res.getMetadata().getLabels()).containsKey(MUTATION_TARGET_LABEL);
  }


}
