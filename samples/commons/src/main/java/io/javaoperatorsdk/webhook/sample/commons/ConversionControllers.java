package io.javaoperatorsdk.webhook.sample.commons;

import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.javaoperatorsdk.webhook.sample.commons.mapper.AsyncV1Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.AsyncV2Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.V1Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.V2Mapper;

public class ConversionControllers {

  public static final String CONVERSION_PATH = "convert";
  public static final String ASYNC_CONVERSION_PATH = "async-convert";

  public static ConversionController conversionController() {
    var controller = new ConversionController();
    controller.registerMapper(new V1Mapper());
    controller.registerMapper(new V2Mapper());
    return controller;
  }


  public static AsyncConversionController asyncConversionController() {
    var controller = new AsyncConversionController();
    controller.registerMapper(new AsyncV1Mapper());
    controller.registerMapper(new AsyncV2Mapper());
    return controller;
  }

}
