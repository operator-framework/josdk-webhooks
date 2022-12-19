package io.javaoperatorsdk.webhook.sample.commons.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceSpecV2;
import io.javaoperatorsdk.webhook.sample.commons.customresource.MultiVersionCustomResourceV2;

@TargetVersion("v2")
public class V2Mapper
    implements Mapper<MultiVersionCustomResourceV2, MultiVersionHub> {

  @Override
  public MultiVersionHub toHub(MultiVersionCustomResourceV2 resource) {
    var hub = new MultiVersionHub();
    hub.setMetadata(resource.getMetadata());
    hub.setValue(Integer.parseInt(resource.getSpec().getAlteredValue()));
    return hub;
  }

  @Override
  public MultiVersionCustomResourceV2 fromHub(
      MultiVersionHub hub) {
    var res = new MultiVersionCustomResourceV2();
    res.setMetadata(hub.getMetadata());
    res.setSpec(new MultiVersionCustomResourceSpecV2());
    res.getSpec().setAlteredValue(Integer.toString(hub.getValue()));
    return res;
  }
}
