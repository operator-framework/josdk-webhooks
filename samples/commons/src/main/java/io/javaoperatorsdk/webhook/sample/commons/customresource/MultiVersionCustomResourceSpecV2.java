package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class MultiVersionCustomResourceSpecV2 {

  private String value;


  public String getValue() {
    return value;
  }

  public MultiVersionCustomResourceSpecV2 setValue(String value) {
    this.value = value;
    return this;
  }

}
