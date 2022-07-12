package io.javaoperatorsdk.webhook.sample.commons.customresource;


public class MultiVersionCustomResourceStatusV2 {

  private Boolean ready;

  private String message;

  public Boolean getReady() {
    return ready;
  }

  public MultiVersionCustomResourceStatusV2 setReady(Boolean ready) {
    this.ready = ready;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public MultiVersionCustomResourceStatusV2 setMessage(String message) {
    this.message = message;
    return this;
  }
}
