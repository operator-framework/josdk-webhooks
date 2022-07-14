package io.javaoperatorsdk.webhook.conversion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;

import static io.javaoperatorsdk.webhook.conversion.Commons.*;

public class ConversionController implements ConversionRequestHandler {

  private static final Logger log = LoggerFactory.getLogger(ConversionController.class);


  @SuppressWarnings("rawtypes")
  private final Map<String, Mapper> mappers = new HashMap<>();

  public void registerMapper(Mapper<?, ?> mapper) {
    String version = mapper.getClass().getDeclaredAnnotation(TargetVersion.class).value();
    if (mappers.get(version) != null) {
      throw new IllegalStateException(MAPPER_ALREADY_REGISTERED_FOR_VERSION_MESSAGE + version);
    }
    mappers.put(version, mapper);
    Utils.registerCustomKind(
        Utils.getFirstTypeArgumentFromInterface(mapper.getClass(), Mapper.class));
  }

  @Override
  public ConversionReview handle(ConversionReview conversionReview) {
    try {
      List<HasMetadata> convertedObjects =
          convertObjects(conversionReview.getRequest().getObjects(),
              Utils.versionOfApiVersion(conversionReview.getRequest().getDesiredAPIVersion()));
      return createResponse(convertedObjects, conversionReview);
    } catch (MissingConversionMapperException e) {
      log.error("Error in conversion hook. UID: {}", conversionReview.getRequest().getUid(), e);
      return createErrorResponse(e, conversionReview);
    }
  }

  @SuppressWarnings("unchecked")
  private List<HasMetadata> convertObjects(List<HasMetadata> objects, String targetVersion) {
    return objects.stream().map(r -> mapObject(r, targetVersion))
        .collect(Collectors.toList());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private HasMetadata mapObject(HasMetadata resource, String targetVersion) {
    String sourceVersion = Utils.versionOfApiVersion(resource.getApiVersion());
    var sourceToHubMapper = mappers.get(sourceVersion);
    if (sourceToHubMapper == null) {
      throwMissingMapperForVersion(sourceVersion);
    }
    var hubToTarget = mappers.get(targetVersion);
    if (hubToTarget == null) {
      throwMissingMapperForVersion(targetVersion);
    }
    return hubToTarget.fromHub(sourceToHubMapper.toHub(resource));
  }

}
