package io.javaoperatorsdk.webhook.conversion.crd;

public class CustomResourceV1Spec {

  private int value;

  public int getValue() {
    return value;
  }

  public CustomResourceV1Spec setValue(int value) {
    this.value = value;
    return this;
  }

}
