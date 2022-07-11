package io.javaoperatorsdk.webhook.sample.commons.customresource;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("sample.javaoperatorsdk")
@Version(value = "v2")
@Kind("MultiVersionCustomResource")
@ShortNames("tcr")
public class TestCustomResourceV2
    extends CustomResource<TestCustomResourceSpecV2, TestCustomResourceStatusV2> {
}
