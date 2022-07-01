package io.javaoperatorsdk.webhook.conversion;

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
}
