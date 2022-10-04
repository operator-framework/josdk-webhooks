package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.fabric8.kubernetes.api.model.ObjectMeta;

public class MultiVersionHub {

  private ObjectMeta metadata = new ObjectMeta();

  private int value;

  public ObjectMeta getMetadata() {
    return metadata;
  }

  public void setMetadata(ObjectMeta metadata) {
    this.metadata = metadata;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
