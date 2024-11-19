package io.javaoperatorsdk.webhook.conversion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TargetVersion {

  /**
   * The target version of the resource this mapper supports. Example values: "v1","v1beta1". This
   * is not the full API Version just the version suffix, for example only the "v1" of api version:
   * "apiextensions.k8s.io/v1"
   *
   * @return version
   **/
  String value();
}
