package io.javaoperatorsdk.webhook.sample.commons.customresource;

public class TestCustomResourceSpec {

    private int value;

    public int getValue() {
        return value;
    }

    public TestCustomResourceSpec setValue(int value) {
        this.value = value;
        return this;
    }
}
