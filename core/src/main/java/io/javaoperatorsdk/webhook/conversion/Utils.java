package io.javaoperatorsdk.webhook.conversion;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;

public class Utils {

  private Utils() {}

  /**
   * @param apiVersion like "apiextensions.k8s.io/v1"
   * @return version suffix; "v1" from the example above
   */
  public static String versionOfApiVersion(String apiVersion) {
    var lastDelimiter = apiVersion.lastIndexOf("/");
    return apiVersion.substring(lastDelimiter + 1);
  }

  public static void registerCustomKind(Class<? extends HasMetadata> clazz) {
    KubernetesDeserializer.registerCustomKind(HasMetadata.getApiVersion(clazz),
        HasMetadata.getKind(clazz), clazz);
  }

  @SuppressWarnings("unchecked")
  public static Class<? extends HasMetadata> getFirstTypeArgumentFromInterface(Class<?> clazz,
      Class<?> expectedImplementedInterface) {
    return (Class<? extends HasMetadata>) Arrays.stream(clazz.getGenericInterfaces())
        .filter(type -> type.getTypeName().startsWith(expectedImplementedInterface.getName())
            && type instanceof ParameterizedType)
        .map(ParameterizedType.class::cast)
        .findFirst()
        .map(t -> (Class<?>) t.getActualTypeArguments()[0])
        .orElseThrow(() -> new RuntimeException(
            "Couldn't retrieve generic parameter type from " + clazz.getSimpleName()
                + " because it doesn't implement "
                + expectedImplementedInterface.getSimpleName()
                + " directly"));
  }
}
