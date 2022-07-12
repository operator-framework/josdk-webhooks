package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class TestCustomResourceSpecV2 {

    private String value;

    private String additionalValue;

    public String getValue() {
        return value;
    }

    public TestCustomResourceSpecV2 setValue(String value) {
        this.value = value;
        return this;
    }
}
