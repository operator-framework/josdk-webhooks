package io.javaoperatorsdk.webhook.admission.sample.quarkus.conversion;

import javax.inject.Singleton;

import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.javaoperatorsdk.webhook.sample.commons.ConversionControllers;

public class ConversionControllerConfig {

  @Singleton
  public ConversionController conversionController() {
    return ConversionControllers.conversionController();
  }

  @Singleton
  public AsyncConversionController asyncConversionController() {
    return ConversionControllers.asyncConversionController();
  }

}
