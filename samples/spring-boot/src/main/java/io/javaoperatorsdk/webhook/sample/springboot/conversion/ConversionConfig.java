package io.javaoperatorsdk.webhook.sample.springboot.conversion;

import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.javaoperatorsdk.webhook.sample.commons.mapper.AsyncV1Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.AsyncV2Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.V1Mapper;
import io.javaoperatorsdk.webhook.sample.commons.mapper.V2Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfig {

    @Bean
    public ConversionController conversionController() {
        var controller = new ConversionController();
        controller.registerMapper(new V1Mapper());
        controller.registerMapper(new V2Mapper());
        return controller;
    }

    @Bean
    public AsyncConversionController asyncConversionController() {
        var controller = new AsyncConversionController();
        controller.registerMapper(new AsyncV1Mapper());
        controller.registerMapper(new AsyncV2Mapper());
        return controller;
    }

}
