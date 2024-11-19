package io.javaoperatorsdk.webhook.conversion.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2Spec;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV2Status;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3Spec;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3Status;

import static io.javaoperatorsdk.webhook.conversion.ConversionTestSupport.DEFAULT_ADDITIONAL_VALUE;
import static io.javaoperatorsdk.webhook.conversion.ConversionTestSupport.DEFAULT_THIRD_VALUE;

@TargetVersion("v2")
public class CustomResourceV2Mapper implements Mapper<CustomResourceV2, CustomResourceV3> {

  @Override
  public CustomResourceV3 toHub(CustomResourceV2 resource) {
    var hubV3 = new CustomResourceV3();
    hubV3.setMetadata(resource.getMetadata());
    var specV3 = new CustomResourceV3Spec();
    specV3.setValue(resource.getSpec().getValue());
    specV3.setAdditionalValue(DEFAULT_ADDITIONAL_VALUE);
    specV3.setThirdValue(DEFAULT_THIRD_VALUE);
    hubV3.setSpec(specV3);
    if (resource.getStatus() != null) {
      hubV3.setStatus(new CustomResourceV3Status());
      hubV3.getStatus().setValue1(resource.getStatus().getValue1());
    }
    return hubV3;
  }

  @Override
  public CustomResourceV2 fromHub(CustomResourceV3 hub) {
    var resourceV2 = new CustomResourceV2();
    resourceV2.setMetadata(hub.getMetadata());
    resourceV2.setSpec(new CustomResourceV2Spec());
    resourceV2.getSpec().setValue(hub.getSpec().getValue());
    resourceV2.getSpec().setAdditionalValue(hub.getSpec().getAdditionalValue());
    if (resourceV2.getStatus() != null) {
      resourceV2.setStatus(new CustomResourceV2Status());
      resourceV2.getStatus().setValue1(hub.getStatus().getValue1());
    }
    return resourceV2;
  }
}
