package io.javaoperatorsdk.webhook.conversion.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.*;

import static io.javaoperatorsdk.webhook.conversion.ConversionTestSupport.DEFAULT_ADDITIONAL_VALUE;
import static io.javaoperatorsdk.webhook.conversion.ConversionTestSupport.DEFAULT_THIRD_VALUE;

@TargetVersion("v2")
public class CustomResourceV2Mapper implements Mapper<CustomResourceV2, CustomResourceV3> {

  @Override
  public CustomResourceV3 toHub(CustomResourceV2 resource) {
    CustomResourceV3 hub = new CustomResourceV3();
    hub.setMetadata(resource.getMetadata());
    var spec = new CustomResourceV3Spec();
    spec.setValue(resource.getSpec().getValue());
    spec.setAdditionalValue(DEFAULT_ADDITIONAL_VALUE);
    spec.setThirdValue(DEFAULT_THIRD_VALUE);
    hub.setSpec(spec);
    if (resource.getStatus() != null) {
      hub.setStatus(new CustomResourceV3Status());
      hub.getStatus().setValue1(resource.getStatus().getValue1());
    }
    return hub;
  }

  @Override
  public CustomResourceV2 fromHub(CustomResourceV3 hub) {
    CustomResourceV2 resource = new CustomResourceV2();
    resource.setMetadata(hub.getMetadata());
    resource.setSpec(new CustomResourceV2Spec());
    resource.getSpec().setValue(hub.getSpec().getValue());
    resource.getSpec().setAdditionalValue(hub.getSpec().getAdditionalValue());
    if (resource.getStatus() != null) {
      resource.setStatus(new CustomResourceV2Status());
      resource.getStatus().setValue1(hub.getStatus().getValue1());
    }
    return resource;
  }
}
