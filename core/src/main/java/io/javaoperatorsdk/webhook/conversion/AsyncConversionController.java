package io.javaoperatorsdk.webhook.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;

import static io.javaoperatorsdk.webhook.conversion.Commons.MAPPER_ALREADY_REGISTERED_FOR_VERSION_MESSAGE;
import static io.javaoperatorsdk.webhook.conversion.Commons.createErrorResponse;
import static io.javaoperatorsdk.webhook.conversion.Commons.createResponse;
import static io.javaoperatorsdk.webhook.conversion.Commons.throwMissingMapperForVersion;

public class AsyncConversionController implements AsyncConversionRequestHandler {

  private static final Logger log = LoggerFactory.getLogger(AsyncConversionController.class);

  @SuppressWarnings("rawtypes")
  private final Map<String, AsyncMapper> mappers = new HashMap<>();

  public void registerMapper(AsyncMapper<?, ?> mapper) {
    var version = mapper.getClass().getDeclaredAnnotation(TargetVersion.class).value();
    if (mappers.get(version) != null) {
      throw new IllegalStateException(MAPPER_ALREADY_REGISTERED_FOR_VERSION_MESSAGE + version);
    }
    mappers.put(version, mapper);
  }

  @Override
  public CompletionStage<ConversionReview> handle(ConversionReview conversionReview) {
    try {
      return convertObjects(
          conversionReview.getRequest().getObjects().stream()
              .map(HasMetadata.class::cast).collect(Collectors.toList()),
          Utils.versionOfApiVersion(conversionReview.getRequest().getDesiredAPIVersion()))
          .thenApply(convertedObjects -> createResponse(convertedObjects, conversionReview));
    } catch (MissingConversionMapperException e) {
      log.error("Error in conversion hook. UID: {}",
          conversionReview.getRequest().getUid(), e);
      return CompletableFuture.completedStage(createErrorResponse(e, conversionReview));
    }
  }

  @SuppressWarnings("unchecked")
  private CompletionStage<List<HasMetadata>> convertObjects(List<HasMetadata> objects,
      String targetVersion) {
    CompletableFuture<HasMetadata>[] completableFutures = new CompletableFuture[objects.size()];
    for (int i = 0; i < objects.size(); i++) {
      completableFutures[i] = mapObject(objects.get(i), targetVersion);
    }
    return CompletableFuture.allOf(completableFutures).thenApply(r -> {
      var result = new ArrayList<HasMetadata>(completableFutures.length);
      for (var cf : completableFutures) {
        result.add(cf.join());
      }
      return result;
    });
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private CompletableFuture<HasMetadata> mapObject(HasMetadata resource, String targetVersion) {
    var sourceVersion = Utils.versionOfApiVersion(resource.getApiVersion());
    var sourceToHubMapper = mappers.get(sourceVersion);
    if (sourceToHubMapper == null) {
      throwMissingMapperForVersion(sourceVersion);
    }
    var hubToTarget = mappers.get(targetVersion);
    if (hubToTarget == null) {
      throwMissingMapperForVersion(targetVersion);
    }
    return sourceToHubMapper.toHub(resource)
        .thenApply(r -> hubToTarget.fromHub(r).toCompletableFuture().join())
        .toCompletableFuture();
  }
}
