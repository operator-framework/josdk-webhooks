package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.*;

@TargetVersion("v1")
public class V1Mapper implements Mapper<MultiVersionCustomResource, MultiVersionCustomResourceV2> {


  public static final String DEFAULT_ADDITIONAL_VALUE = "default_additional_value";

  @Override
  public MultiVersionCustomResourceV2 toHub(MultiVersionCustomResource resource) {
    var hub = new MultiVersionCustomResourceV2();
    hub.setMetadata(resource.getMetadata());

    var spec = new MultiVersionCustomResourceSpecV2();
    spec.setValue(String.valueOf(resource.getSpec().getValue()));
    spec.setAdditionalValue(DEFAULT_ADDITIONAL_VALUE);
    hub.setSpec(spec);


    if (resource.getStatus() != null) {
      var status = new MultiVersionCustomResourceStatusV2();
      status.setReady(resource.getStatus().getReady());
      hub.setStatus(status);
    }
    return hub;
  }

  @Override
  public MultiVersionCustomResource fromHub(MultiVersionCustomResourceV2 hub) {
    var res = new MultiVersionCustomResource();
    res.setMetadata(hub.getMetadata());

    var spec = new MultiVersionCustomResourceSpec();
    spec.setValue(Integer.parseInt(hub.getSpec().getValue()));
    res.setSpec(spec);

    if (hub.getStatus() != null) {
      var status = new MultiVersionCustomResourceStatus();
      status.setReady(hub.getStatus().getReady());
      res.setStatus(status);
    }
    return res;
  }
}
