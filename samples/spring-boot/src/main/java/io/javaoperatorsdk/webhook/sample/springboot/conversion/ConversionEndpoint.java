package io.javaoperatorsdk.webhook.sample.springboot.conversion;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;

import reactor.core.publisher.Mono;

import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.ASYNC_CONVERSION_PATH;
import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.CONVERSION_PATH;

@RestController
public class ConversionEndpoint {

  private final ConversionController conversionController;
  private final AsyncConversionController asyncConversionController;

  public ConversionEndpoint(ConversionController conversionController,
      AsyncConversionController asyncConversionController) {
    this.conversionController = conversionController;
    this.asyncConversionController = asyncConversionController;
  }

  @PostMapping(CONVERSION_PATH)
  @ResponseBody
  public ConversionReview convert(@RequestBody ConversionReview conversionReview) {
    return conversionController.handle(conversionReview);
  }

  @PostMapping(ASYNC_CONVERSION_PATH)
  @ResponseBody
  public Mono<ConversionReview> convertAsync(@RequestBody ConversionReview conversionReview) {
    return Mono.fromCompletionStage(asyncConversionController.handle(conversionReview));
  }

}
