package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.*;

@TargetVersion("v1")
public class V1Mapper implements Mapper<TestCustomResource, TestCustomResourceV2> {


  @Override
  public TestCustomResourceV2 toHub(TestCustomResource resource) {
    var hub = new TestCustomResourceV2();
    hub.setMetadata(resource.getMetadata());

    var spec= new TestCustomResourceSpecV2();
    spec.setValue(String.valueOf(resource.getSpec().getValue()));
    hub.setSpec(spec);

    if (resource.getStatus() != null) {
      var status = new TestCustomResourceStatusV2();
      status.setReady(resource.getStatus().getReady());
      hub.setStatus(status);
    }
    return hub;
  }

  @Override
  public TestCustomResource fromHub(TestCustomResourceV2 hub) {
    var res = new TestCustomResource();
    res.setMetadata(hub.getMetadata());

    var spec = new TestCustomResourceSpec();
    spec.setValue(Integer.parseInt(hub.getSpec().getValue()));
    res.setSpec(spec);

    if (hub.getStatus() != null) {
      var status = new TestCustomResourceStatus();
      status.setReady(hub.getStatus().getReady());
      res.setStatus(status);
    }
    return res;
  }
}
