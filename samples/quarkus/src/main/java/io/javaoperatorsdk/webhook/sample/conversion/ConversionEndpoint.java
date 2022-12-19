package io.javaoperatorsdk.webhook.sample.conversion;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;
import io.javaoperatorsdk.webhook.conversion.AsyncConversionController;
import io.javaoperatorsdk.webhook.conversion.ConversionController;
import io.smallrye.mutiny.Uni;

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
