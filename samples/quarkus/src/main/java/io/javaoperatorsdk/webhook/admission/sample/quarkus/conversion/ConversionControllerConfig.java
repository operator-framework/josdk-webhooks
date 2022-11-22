package io.javaoperatorsdk.webhook.admission.sample.quarkus.conversion;

import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.javaoperatorsdk.webhook.sample.commons.ConversionControllers;

import javax.inject.Singleton;

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
