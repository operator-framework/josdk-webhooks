package io.javaoperatorsdk.webhook.conversion.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("sample.javaoperatorsdk")
@Version(value = "v3", storage = false)
@ShortNames("mv3")
public class CustomResourceV3
    extends
    CustomResource<CustomResourceV3Spec, CustomResourceV3Status>
    implements Namespaced {

}
