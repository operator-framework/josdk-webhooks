package io.javaoperatorsdk.webhook.conversion.crd;

public class CustomResourceV2Spec {

  private String value;

  private String additionalValue;

  public String getValue() {
    return value;
  }

  public CustomResourceV2Spec setValue(String value) {
    this.value = value;
    return this;
  }

  public String getAdditionalValue() {
    return additionalValue;
  }

  public CustomResourceV2Spec setAdditionalValue(String additionalValue) {
    this.additionalValue = additionalValue;
    return this;
  }
}
