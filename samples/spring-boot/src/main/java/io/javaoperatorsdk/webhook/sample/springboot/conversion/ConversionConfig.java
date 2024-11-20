package io.javaoperatorsdk.webhook.sample.springboot.conversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.javaoperatorsdk.webhook.sample.commons.ConversionControllers;

@Configuration
public class ConversionConfig {

  @Bean
  public ConversionController conversionController() {
    return ConversionControllers.conversionController();
  }

  @Bean
  public AsyncConversionController asyncConversionController() {
    return ConversionControllers.asyncConversionController();
  }
}
