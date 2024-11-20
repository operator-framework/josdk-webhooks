package io.javaoperatorsdk.webhook.conversion;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;

public interface ConversionRequestHandler {

  ConversionReview handle(ConversionReview admissionRequest);
}
