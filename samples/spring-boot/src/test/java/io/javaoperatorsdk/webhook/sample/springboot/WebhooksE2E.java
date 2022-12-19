package io.javaoperatorsdk.webhook.sample.springboot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.*;
import io.fabric8.kubernetes.api.model.networking.v1.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceSpec;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

import static io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers.MUTATION_TARGET_LABEL;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.*;
import static io.javaoperatorsdk.webhook.sample.springboot.conversion.ConversionEndpoint.CONVERSION_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WebhooksE2E {

  private KubernetesClient client = new KubernetesClientBuilder().build();

  public static final String TEST_CR_NAME = "test-cr";
  public static final int CR_SPEC_VALUE = 5;

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
      applyAndWait(client,
          "../commons/target/classes/META-INF/fabric8/multiversioncustomresources.sample.javaoperatorsdk-v1.yml",
          addConversionHookEndpointToCustomResource());
    }
  }

  @Test
  void validationHook() {
    var ingressWithLabel = testIngress("normal-add-test");
    addRequiredLabels(ingressWithLabel);
    await().atMost(Duration.ofSeconds(SPIN_UP_GRACE_PERIOD)).untilAsserted(() -> {
      Ingress res = null;
      try {
        // this can be since coredns in minikube can take some time
        res = client.network().v1().ingresses().resource(ingressWithLabel).createOrReplace();
      } catch (KubernetesClientException e) {
      }
      assertThat(res).isNotNull();
    });
    assertThrows(KubernetesClientException.class,
        () -> client.network().v1().ingresses().resource(testIngress("validate-test"))
            .createOrReplace());
  }

  @Test
  void mutationHook() {
    var ingressWithLabel = testIngress("mutation-test");
    addRequiredLabels(ingressWithLabel);
    await().atMost(Duration.ofSeconds(SPIN_UP_GRACE_PERIOD)).untilAsserted(() -> {
      Ingress res = null;
      try {
        res = client.network().v1().ingresses().resource(ingressWithLabel).createOrReplace();
      } catch (KubernetesClientException e) {
      }
      assertThat(res).isNotNull();
      assertThat(res.getMetadata().getLabels()).containsKey(MUTATION_TARGET_LABEL);
    });
  }

  @Test
  void conversionHook() {
    await().atMost(Duration.ofSeconds(SPIN_UP_GRACE_PERIOD)).untilAsserted(() -> {
      try {
        // this can be since coredns in minikube can take some time
        createV1Resource(TEST_CR_NAME);
      } catch (KubernetesClientException e) {
      }
    });
    MultiVersionCustomResourceV2 v2 =
        client.resources(MultiVersionCustomResourceV2.class).withName(TEST_CR_NAME).get();
    assertThat(v2.getSpec().getAlteredValue()).isEqualTo("" + CR_SPEC_VALUE);
  }

  private MultiVersionCustomResource createV1Resource(String name) {
    var res = new MultiVersionCustomResource();
    res.setMetadata(new ObjectMetaBuilder()
        .withName(name)
        .build());
    res.setSpec(new MultiVersionCustomResourceSpec());
    res.getSpec().setValue(CR_SPEC_VALUE);
    return client.resource(res).createOrReplace();
  }

  static UnaryOperator<HasMetadata> addConversionHookEndpointToCustomResource() {
    return r -> {
      if (!(r instanceof CustomResourceDefinition)) {
        return r;
      }
      var crd = (CustomResourceDefinition) r;
      var crc = new CustomResourceConversion();
      crd.getMetadata()
          .setAnnotations(Map.of("cert-manager.io/inject-ca-from", "default/spring-boot-sample"));
      crd.getSpec().setConversion(crc);
      crc.setStrategy("Webhook");

      var whc = new WebhookConversionBuilder()
          .withConversionReviewVersions(List.of("v1"))
          .withClientConfig(new WebhookClientConfigBuilder()
              .withService(new ServiceReferenceBuilder()
                  .withPath("/" + CONVERSION_PATH)
                  .withName("spring-boot-sample")
                  .withNamespace("default")
                  .build())
              .build())
          .build();
      crc.setWebhook(whc);
      return crd;
    };
  }
}
