package io.javaoperatorsdk.webhook.sample;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResource;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceSpec;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

import static io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers.MUTATION_TARGET_LABEL;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.SPIN_UP_GRACE_PERIOD;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.addRequiredLabels;
import static io.javaoperatorsdk.webhook.sample.commons.Utils.testIngress;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("deprecation")
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
    await().atMost(Duration.ofSeconds(SPIN_UP_GRACE_PERIOD))
        .untilAsserted(() -> avoidRequestTimeout(() -> createV1Resource(TEST_CR_NAME)));
    MultiVersionCustomResourceV2 v2 =
        client.resources(MultiVersionCustomResourceV2.class).withName(TEST_CR_NAME).get();
    assertThat(v2.getSpec().getAlteredValue()).isEqualTo("" + CR_SPEC_VALUE);
  }

  @SuppressWarnings("SameParameterValue")
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
}
