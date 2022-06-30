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

  public static final String VERSION_DELIMITER = "#";

  private final String hubVersion;

  private Map<String, Mapper> toHubMappers;
  private Map<String, Mapper> fromHubMappers;
  private Map<String, Mapper> directMappers = new HashMap<>();

  /**
   * Initialize without a hub version.
   */
  public DefaultConversionService() {
    this(null);
  }

  public DefaultConversionService(String hubVersion) {
    this.hubVersion = hubVersion;
    if (isHubSupported()) {
      toHubMappers = new HashMap<>();
      fromHubMappers = new HashMap<>();
    }
  }

  public void registerMapper(Mapper<?, ?> mapper) {
    directMappers.put(combinedVersionIdentifier(mapper.sourceVersion(), mapper.targetVersion()),
        mapper);
    if (isHubSupported()) {
      if (mapper.sourceVersion().equals(hubVersion)) {
        fromHubMappers.put(mapper.targetVersion(), mapper);
      } else if (mapper.targetVersion().equals(hubVersion)) {
        toHubMappers.put(mapper.sourceVersion(), mapper);
      } else {
        log.warn(
            "Hub supported, but neither source ({}) or target ({}) version of mapper equals to hub ({}) version",
            mapper.sourceVersion(), mapper.targetVersion(), hubVersion);
      }
    }
  }

  private String combinedVersionIdentifier(String sourceVersion, String targetVersion) {
    return sourceVersion + VERSION_DELIMITER + targetVersion;
  }

  @Override
  public ConversionReview handle(ConversionReview conversionReview) {
    try {
      List<HasMetadata> convertedObjects =
          convertObjects(conversionReview.getRequest().getObjects(),
              conversionReview.getRequest().getDesiredAPIVersion());

      return createResponse(convertedObjects, conversionReview);
    } catch (RuntimeException e) {
      // todo
      return null;
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

  private ConversionReview createErrorResponse(Exception e,ConversionReview conversionReview) {
    ConversionReview result = new ConversionReview();

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
    Mapper directMapper =
        directMappers.get(combinedVersionIdentifier(sourceVersion, targetVersion));
    if (directMapper != null) {
      return directMapper.map(resource);
    }
    if (!isHubSupported()) {
      throw new MissingConversionMapperException(
          "Missing direct conversion mapper, from version: " + sourceVersion
              + ", to version:" + targetVersion);
    }
    if (targetVersion.equals(hubVersion)) {
      return toHubMappers.get(sourceVersion).map(resource);
    } else if (sourceVersion.equals(hubVersion)) {
      return fromHubMappers.get(targetVersion).map(resource);
    } else {
      var toHubMapper = toHubMappers.get(sourceVersion);
      if (toHubMapper == null) {
        throw new MissingConversionMapperException(
            "Missing to hub mapper from version: " + sourceVersion);
      }
      HasMetadata hubResource = toHubMapper.map(resource);
      var fromHubMapper = fromHubMappers.get(targetVersion);
      if (fromHubMapper == null) {
        throw new MissingConversionMapperException(
            "Missing mapper from hub to version: " + targetVersion);
      }
      return fromHubMapper.map(hubResource);
    }
  }



  private boolean isHubSupported() {
    return hubVersion != null;
  }
}
