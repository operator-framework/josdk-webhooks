package io.javaoperatorsdk.webhook.sample.commons.customresource;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("sample.javaoperatorsdk")
@Version(value = "v1", storage = false)
@ShortNames("tcr")
public class TestCustomResource
    extends CustomResource<TestCustomResourceSpec, TestCustomResourceStatus> {

}
