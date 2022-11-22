package io.javaoperatorsdk.webhook.admission.sample.quarkus.conversion;

import javax.inject.Singleton;

import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.javaoperatorsdk.webhook.sample.commons.ConversionControllers;
import io.javaoperatorsdk.webhook.sample.commons.mapper.V1Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.V2Mapper;

public class ConversionControllerConfig {

  @Singleton
  public ConversionController conversionController() {
    var controller = new ConversionController();
    controller.registerMapper(new V1Mapper());
    controller.registerMapper(new V2Mapper());
    return controller;
  }

  @Singleton
  public AsyncConversionController asyncConversionController() {
    return ConversionControllers.asyncConversionController();
  }

}
