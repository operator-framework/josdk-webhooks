package io.javaoperatorsdk.webhook.sample;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceSpec;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

import static io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers.MUTATION_TARGET_LABEL;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.*;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.SPIN_UP_GRACE_PERIOD;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EndToEndTestBase {

  protected KubernetesClient client = new KubernetesClientBuilder().build();

  public static final String TEST_CR_NAME = "test-cr";
  public static final int CR_SPEC_VALUE = 5;

  @Test
  void validationHook() {
    var ingressWithLabel = testIngress("normal-add-test");
    addRequiredLabels(ingressWithLabel);
    await().atMost(Duration.ofSeconds(SPIN_UP_GRACE_PERIOD)).untilAsserted(() -> {
      var res = avoidRequestTimeout(
          () -> client.network().v1().ingresses().resource(ingressWithLabel).createOrReplace());
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
      var res = avoidRequestTimeout(
          () -> client.network().v1().ingresses().resource(ingressWithLabel).createOrReplace());
      assertThat(res).isNotNull();
      assertThat(res.getMetadata().getLabels()).containsKey(MUTATION_TARGET_LABEL);
    });
  }

  @Test
  void conversionHook() {
    await().atMost(Duration.ofSeconds(SPIN_UP_GRACE_PERIOD)).untilAsserted(() -> {
      avoidRequestTimeout(() -> createV1Resource(TEST_CR_NAME));

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

  <T> T avoidRequestTimeout(Supplier<T> operator) {
    try {
      return operator.get();
    } catch (KubernetesClientException e) {
      return null;
    }
  }

  /** On minikube CoreDNS can take some time to start */
  public static void waitForCoreDNS(KubernetesClient client) {
    client.apps().deployments().inNamespace("kube-system").withName("coredns").waitUntilReady(2,
        TimeUnit.MINUTES);
  }

  public static UnaryOperator<HasMetadata> addConversionHookEndpointToCustomResource(
      String serviceName) {
    return r -> {
      if (!(r instanceof CustomResourceDefinition)) {
        return r;
      }
      var crd = (CustomResourceDefinition) r;
      var crc = new CustomResourceConversion();
      crd.getMetadata()
          .setAnnotations(Map.of("cert-manager.io/inject-ca-from", "default/" + serviceName));
      crd.getSpec().setConversion(crc);
      crc.setStrategy("Webhook");

      var whc = new WebhookConversionBuilder()
          .withConversionReviewVersions(List.of("v1"))
          .withClientConfig(new WebhookClientConfigBuilder()
              .withService(new ServiceReferenceBuilder()
                  .withPath("/")
                  .withName(serviceName)
                  .withNamespace("default")
                  .withPort(443)
                  .build())
              .build())
          .build();
      crc.setWebhook(whc);
      return crd;
    };
  }
}
