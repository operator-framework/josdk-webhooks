package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class MultiVersionCustomResourceSpecV2 {

  private String alteredValue;

  public String getAlteredValue() {
    return alteredValue;
  }

  public MultiVersionCustomResourceSpecV2 setAlteredValue(String alteredValue) {
    this.alteredValue = alteredValue;
    return this;
  }

}
