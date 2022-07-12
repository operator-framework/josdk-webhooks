package io.javaoperatorsdk.webhook.sample.springboot.conversion;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ConversionEndpoint {

    public static final String CONVERSION_PATH = "convert";
    public static final String ASYNC_CONVERSION_PATH = "async-convert";

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
