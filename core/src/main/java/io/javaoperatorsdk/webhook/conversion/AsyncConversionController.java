package io.javaoperatorsdk.webhook.conversion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apiextensions.v1.ConversionReview;

import static io.javaoperatorsdk.webhook.conversion.Commons.*;

public class AsyncConversionController implements AsyncConversionRequestHandler {

  private static final Logger log = LoggerFactory.getLogger(ConversionController.class);

  @SuppressWarnings("rawtypes")
  private final Map<String, AsyncMapper> mappers = new HashMap<>();

  public void registerMapper(AsyncMapper<?, ?> mapper) {
    String version = mapper.getClass().getDeclaredAnnotation(TargetVersion.class).value();
    mappers.put(version, mapper);
  }

  @Override
  public CompletionStage<ConversionReview> handle(ConversionReview conversionReview) {

    return convertObjects(
        conversionReview.getRequest().getObjects(),
        Utils.versionOfApiVersion(conversionReview.getRequest().getDesiredAPIVersion()))
            .thenApply(convertedObjects -> createResponse(convertedObjects, conversionReview))
            .exceptionally(e -> {
              if (e instanceof MissingConversionMapperException) {
                return createErrorResponse((Exception) e, conversionReview);
              } else {
                throw new IllegalStateException(e);
              }
            });

  }

  @SuppressWarnings("unchecked")
  private CompletionStage<List<HasMetadata>> convertObjects(List<HasMetadata> objects,
      String targetVersion) {
    CompletableFuture<HasMetadata>[] completableFutures = new CompletableFuture[objects.size()];
    for (int i = 0; i < objects.size(); i++) {
      completableFutures[i] = mapObject(objects.get(i), targetVersion);
    }
    return CompletableFuture.allOf(completableFutures).thenApply(r -> Stream.of(completableFutures)
        .map(CompletableFuture::join).collect(Collectors.toList()));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private CompletableFuture<HasMetadata> mapObject(HasMetadata resource, String targetVersion) {
    String sourceVersion = Utils.versionOfApiVersion(resource.getApiVersion());
    AsyncMapper sourceToHubMapper = mappers.get(sourceVersion);
    if (sourceToHubMapper == null) {
      throwMissingMapperForVersion(sourceVersion);
    }
    AsyncMapper hubToTarget = mappers.get(targetVersion);
    if (hubToTarget == null) {
      throwMissingMapperForVersion(targetVersion);
    }
    return sourceToHubMapper.toHub(resource).thenApply(hubToTarget::fromHub).toCompletableFuture();

  }

}
