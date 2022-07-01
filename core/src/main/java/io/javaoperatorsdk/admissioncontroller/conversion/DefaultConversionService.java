package io.javaoperatorsdk.admissioncontroller.conversion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionResponse;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;

public class DefaultConversionService implements ConversionRequestHandler {

  private static final Logger log = LoggerFactory.getLogger(DefaultConversionService.class);
  public static final String FAILED_STATUS_MESSAGE = "Failed";

  private Map<String, Mapper> mappers = new HashMap<>();

  public void registerMapper(Mapper<?, ?> mapper) {
    mappers.put(mapper.version(), mapper);
  }

  @Override
  public ConversionReview handle(ConversionReview conversionReview) {
    try {
      List<HasMetadata> convertedObjects =
          convertObjects(conversionReview.getRequest().getObjects(),
              conversionReview.getRequest().getDesiredAPIVersion());
      return createResponse(convertedObjects, conversionReview);
    } catch (RuntimeException e) {
      log.error("Error in conversion hook. UID: {}", conversionReview.getRequest().getUid(), e);
      return createErrorResponse(e, conversionReview);
    }
  }

  private ConversionReview createResponse(List<HasMetadata> convertedObjects,
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

  private ConversionReview createErrorResponse(Exception e, ConversionReview conversionReview) {
    ConversionReview result = new ConversionReview();
    var response = new ConversionResponse();
    response.setUid(conversionReview.getRequest().getUid());
    response.setResult(new Status());
    response.getResult().setStatus(FAILED_STATUS_MESSAGE);
    response.getResult().setMessage(e.getMessage());
    result.setResponse(response);
    return result;
  }

  @SuppressWarnings("unchecked")
  private List<HasMetadata> convertObjects(List<HasMetadata> objects, String targetVersion) {
    return objects.stream().map(r -> mapObject(r, targetVersion))
        .collect(Collectors.toList());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private HasMetadata mapObject(HasMetadata resource, String targetVersion) {
    String sourceVersion = resource.getApiVersion();

    var sourceToHubMapper = mappers.get(sourceVersion);
    if (sourceToHubMapper == null) {
      throwMissingMapperForVersion(sourceVersion);
    }
    var hubToTarget = mappers.get(targetVersion);
    if (hubToTarget == null) {
      throwMissingMapperForVersion(targetVersion);
    }
    return hubToTarget.fromHub(sourceToHubMapper.fromHub(resource));
  }

  private void throwMissingMapperForVersion(String version) {
    throw new MissingConversionMapperException(
        "Missing mapper from version: " + version);
  }

}
