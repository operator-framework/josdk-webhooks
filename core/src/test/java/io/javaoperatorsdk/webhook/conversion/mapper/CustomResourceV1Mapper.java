package io.javaoperatorsdk.webhook.conversion.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.*;

import static io.javaoperatorsdk.webhook.conversion.ConversionControllerTest.DEFAULT_ADDITIONAL_VALUE;
import static io.javaoperatorsdk.webhook.conversion.ConversionControllerTest.DEFAULT_THIRD_VALUE;

@TargetVersion("v1")
public class CustomResourceV1Mapper implements Mapper<CustomResourceV1, CustomResourceV3> {

  @Override
  public CustomResourceV3 toHub(CustomResourceV1 resource) {
    CustomResourceV3 hub = new CustomResourceV3();
    hub.setMetadata(resource.getMetadata());
    var spec = new CustomResourceV3Spec();
    spec.setValue(Integer.toString(resource.getSpec().getValue()));
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
  public CustomResourceV1 fromHub(CustomResourceV3 hub) {
    CustomResourceV1 resource = new CustomResourceV1();
    resource.setMetadata(hub.getMetadata());
    resource.setSpec(new CustomResourceV1Spec());
    resource.getSpec().setValue(Integer.parseInt(hub.getSpec().getValue()));
    if (resource.getStatus() != null) {
      resource.setStatus(new CustomResourceV1Status());
      resource.getStatus().setValue1(hub.getStatus().getValue1());
    }
    return resource;
  }
}
