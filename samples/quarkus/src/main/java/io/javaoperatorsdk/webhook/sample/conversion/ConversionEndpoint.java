package io.javaoperatorsdk.webhook.sample.conversion;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.ASYNC_CONVERSION_PATH;
import static io.javaoperatorsdk.webhook.sample.commons.ConversionControllers.CONVERSION_PATH;

@Path("/")
public class ConversionEndpoint {

  private final ConversionController conversionController;
  private final AsyncConversionController asyncConversionController;

  public ConversionEndpoint(ConversionController conversionController,
      AsyncConversionController asyncConversionController) {
    this.conversionController = conversionController;
    this.asyncConversionController = asyncConversionController;
  }

  @POST
  @Path(CONVERSION_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ConversionReview convert(ConversionReview conversionReview) {
    return conversionController.handle(conversionReview);
  }

  @POST
  @Path(ASYNC_CONVERSION_PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<ConversionReview> convertAsync(ConversionReview conversionReview) {
    return Uni.createFrom()
        .completionStage(() -> asyncConversionController.handle(conversionReview));
  }
}
