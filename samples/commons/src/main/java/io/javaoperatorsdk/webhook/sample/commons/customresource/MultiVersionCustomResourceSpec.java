package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class MultiVersionCustomResourceSpec {

  private int value;

  public int getValue() {
    return value;
  }

  public MultiVersionCustomResourceSpec setValue(int value) {
    this.value = value;
    return this;
  }
}
