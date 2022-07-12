package io.javaoperatorsdk.webhook.sample.commons.customresource;


public class TestCustomResourceStatusV2 {

    private Boolean ready;

    private String message;

    public Boolean getReady() {
        return ready;
    }

    public TestCustomResourceStatusV2 setReady(Boolean ready) {
        this.ready = ready;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TestCustomResourceStatusV2 setMessage(String message) {
        this.message = message;
        return this;
    }
}
