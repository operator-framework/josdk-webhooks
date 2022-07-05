package io.javaoperatorsdk.webhook.conversion.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("sample.javaoperatorsdk")
@Version(value = "v2", storage = false)
@ShortNames("mv2")
public class CustomResourceV2
    extends
    CustomResource<CustomResourceV2Spec, CustomResourceV2Status>
    implements Namespaced {

}
