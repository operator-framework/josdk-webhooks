package io.javaoperatorsdk.webhook.sample.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.networking.v1.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import static io.javaoperatorsdk.webhook.sample.commons.AdmissionControllers.VALIDATION_TARGET_LABEL;

public class Utils {

  public static final int SPIN_UP_GRACE_PERIOD = 120;


  public static void applyAndWait(KubernetesClient client, String path) {
    applyAndWait(client, path, null);
  }

  public static void applyAndWait(KubernetesClient client, String path,
      UnaryOperator<HasMetadata> transform) {
    try (FileInputStream fileInputStream = new FileInputStream(path)) {
      applyAndWait(client, fileInputStream, transform);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void applyAndWait(KubernetesClient client, InputStream is) {
    applyAndWait(client, is, null);
  }

  public static void applyAndWait(KubernetesClient client, InputStream is,
      UnaryOperator<HasMetadata> transfor) {
    var resources = client.load(is).get();
    if (transfor != null) {
      resources = resources.stream().map(transfor).collect(Collectors.toList());
    }
    client.resourceList(resources).createOrReplace();
    client.resourceList(resources).waitUntilReady(3, TimeUnit.MINUTES);
  }

  public static void addRequiredLabels(Ingress ingress) {
    ingress.getMetadata().setLabels(Map.of(VALIDATION_TARGET_LABEL, "val"));
  }

  public static Ingress testIngress(String name) {
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
                        .withPathType("Prefix")
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

}
