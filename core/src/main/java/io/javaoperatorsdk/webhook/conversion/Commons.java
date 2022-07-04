package io.javaoperatorsdk.webhook.conversion;

import java.util.List;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionResponse;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;

public class Commons {

  public static final String FAILED_STATUS_MESSAGE = "Failed";

  public static ConversionReview createResponse(List<HasMetadata> convertedObjects,
      ConversionReview conversionReview) {
    ConversionReview result = new ConversionReview();
    var response = new ConversionResponse();
    response.setResult(new Status());
    response.getResult().setStatus("Success");
    response.setUid(conversionReview.getRequest().getUid());
    response.setConvertedObjects(convertedObjects);
    result.setResponse(response);
    return result;
  }

  public static ConversionReview createErrorResponse(Exception e,
      ConversionReview conversionReview) {
    ConversionReview result = new ConversionReview();
    var response = new ConversionResponse();
    response.setUid(conversionReview.getRequest().getUid());
    response.setResult(new Status());
    response.getResult().setStatus(FAILED_STATUS_MESSAGE);
    response.getResult().setMessage(e.getMessage());
    result.setResponse(response);
    return result;
  }

  public static void throwMissingMapperForVersion(String version) {
    throw new MissingConversionMapperException(
        "Missing mapper from version: " + version);
  }

}
