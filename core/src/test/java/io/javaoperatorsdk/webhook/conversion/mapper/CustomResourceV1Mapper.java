package io.javaoperatorsdk.webhook.conversion.mapper;

import io.javaoperatorsdk.webhook.conversion.Mapper;
import io.javaoperatorsdk.webhook.conversion.TargetVersion;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1Spec;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV1Status;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3Spec;
import io.javaoperatorsdk.webhook.conversion.crd.CustomResourceV3Status;

import static io.javaoperatorsdk.webhook.conversion.ConversionTestSupport.DEFAULT_ADDITIONAL_VALUE;
import static io.javaoperatorsdk.webhook.conversion.ConversionTestSupport.DEFAULT_THIRD_VALUE;

@TargetVersion("v1")
public class CustomResourceV1Mapper implements Mapper<CustomResourceV1, CustomResourceV3> {

  @Override
  public CustomResourceV3 toHub(CustomResourceV1 resource) {
    var hubV3 = new CustomResourceV3();
    hubV3.setMetadata(resource.getMetadata());
    var specV3 = new CustomResourceV3Spec();
    specV3.setValue(Integer.toString(resource.getSpec().getValue()));
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
  public CustomResourceV1 fromHub(CustomResourceV3 hub) {
    var resourceV1 = new CustomResourceV1();
    resourceV1.setMetadata(hub.getMetadata());
    resourceV1.setSpec(new CustomResourceV1Spec());
    resourceV1.getSpec().setValue(Integer.parseInt(hub.getSpec().getValue()));
    if (resourceV1.getStatus() != null) {
      resourceV1.setStatus(new CustomResourceV1Status());
      resourceV1.getStatus().setValue1(hub.getStatus().getValue1());
    }
    return resourceV1;
  }
}
