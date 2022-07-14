package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class MultiVersionCustomResourceStatus {

  private Boolean ready;

  public Boolean getReady() {
    return ready;
  }

  public MultiVersionCustomResourceStatus setReady(Boolean ready) {
    this.ready = ready;
    return this;
  }
}

