package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class MultiVersionCustomResourceSpecV2 {

  private String changedValueName;

  public String getChangedValueName() {
    return changedValueName;
  }

  public MultiVersionCustomResourceSpecV2 setChangedValueName(String changedValueName) {
    this.changedValueName = changedValueName;
    return this;
  }

}
