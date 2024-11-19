package io.javaoperatorsdk.webhook.conversion.crd;

public class CustomResourceV3Spec {

  private String value;
  private String additionalValue;
  private String thirdValue;

  public String getValue() {
    return value;
  }

  public CustomResourceV3Spec setValue(String value) {
    this.value = value;
    return this;
  }

  public String getAdditionalValue() {
    return additionalValue;
  }

  public CustomResourceV3Spec setAdditionalValue(String additionalValue) {
    this.additionalValue = additionalValue;
    return this;
  }

  public String getThirdValue() {
    return thirdValue;
  }

  public CustomResourceV3Spec setThirdValue(String thirdValue) {
    this.thirdValue = thirdValue;
    return this;
  }
}
