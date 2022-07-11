package io.javaoperatorsdk.webhook.conversion.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("sample.javaoperatorsdk")
@Version("v1")
@Kind("MultiVersionTestCustomResource")
@ShortNames("mv1")
public class CustomResourceV1
    extends
    CustomResource<CustomResourceV1Spec, CustomResourceV1Status>
    implements Namespaced {


}
