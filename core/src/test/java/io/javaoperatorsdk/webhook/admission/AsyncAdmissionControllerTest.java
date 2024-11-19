package io.javaoperatorsdk.webhook.admission;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;
import io.javaoperatorsdk.webhook.admission.mutation.Mutator;
import io.javaoperatorsdk.webhook.admission.validation.Validator;

import static io.javaoperatorsdk.webhook.admission.AdmissionTestSupport.LABEL_KEY;
import static io.javaoperatorsdk.webhook.admission.AdmissionTestSupport.LABEL_TEST_VALUE;
import static io.javaoperatorsdk.webhook.admission.AdmissionTestSupport.MISSING_REQUIRED_LABEL;

class AsyncAdmissionControllerTest {

  AdmissionTestSupport admissionTestSupport = new AdmissionTestSupport();

  @Test
  void validatesResource() {
    var admissionController = new AsyncAdmissionController<HasMetadata>((resource, operation) -> {
    });

    admissionTestSupport
        .validatesResource(res -> admissionController.handle(res).toCompletableFuture().join());
  }

  @Test
  void validatesResource_whenNotAllowedException() {
    var admissionController =
        new AsyncAdmissionController<>((Validator<HasMetadata>) (resource, operation) -> {
          throw new NotAllowedException(MISSING_REQUIRED_LABEL);
        });

    admissionTestSupport
        .notAllowedException(res -> admissionController.handle(res).toCompletableFuture().join());
  }

  @Test
  void validatesResource_whenOtherException() {
    var admissionController =
        new AsyncAdmissionController<>((Validator<HasMetadata>) (resource, operation) -> {
          throw new IllegalArgumentException("Invalid resource");
        });

    admissionTestSupport.assertThatThrownBy(
        res -> admissionController.handle(res).toCompletableFuture()
            .join())
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(IllegalStateException.class)
        .hasRootCauseInstanceOf(IllegalArgumentException.class)
        .hasRootCauseMessage("Invalid resource");
  }

  @Test
  void mutatesResource_withMutator() {
    var admissionController =
        new AsyncAdmissionController<>((Mutator<HasMetadata>) (resource,
            operation) -> {
          resource.getMetadata().getLabels().putIfAbsent(LABEL_KEY, LABEL_TEST_VALUE);
          return resource;
        });

    admissionTestSupport
        .mutatesResource(res -> admissionController.handle(res).toCompletableFuture().join());
  }

  @Test
  void mutatesResource_withAsyncMutator() {
    var admissionController =
        new AsyncAdmissionController<>((AsyncMutator<HasMetadata>) (resource,
            operation) -> CompletableFuture.supplyAsync(() -> {
              resource.getMetadata().getLabels().putIfAbsent(LABEL_KEY, LABEL_TEST_VALUE);
              return resource;
            }));

    admissionTestSupport
        .mutatesResource(res -> admissionController.handle(res).toCompletableFuture().join());
  }

  @Test
  void mutatesResource_withMutator_whenNotAllowedException() {
    var admissionController =
        new AsyncAdmissionController<>((Mutator<HasMetadata>) (resource,
            operation) -> {
          throw new NotAllowedException(MISSING_REQUIRED_LABEL);
        });

    admissionTestSupport.notAllowedException(
        res -> admissionController.handle(res).toCompletableFuture().join());
  }

  @Test
  void mutatesResource_withAsyncMutator_whenNotAllowedException() {
    var admissionController =
        new AsyncAdmissionController<>((AsyncMutator<HasMetadata>) (resource,
            operation) -> CompletableFuture.supplyAsync(() -> {
              throw new NotAllowedException(MISSING_REQUIRED_LABEL);
            }));

    admissionTestSupport.notAllowedException(
        res -> admissionController.handle(res).toCompletableFuture().join());
  }

  @Test
  void mutatesResource_withMutator_whenOtherException() {
    var admissionController =
        new AsyncAdmissionController<>((Mutator<HasMetadata>) (resource,
            operation) -> {
          throw new IllegalArgumentException("Invalid resource");
        });

    admissionTestSupport.assertThatThrownBy(
        res -> admissionController.handle(res).toCompletableFuture()
            .join())
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(IllegalStateException.class)
        .hasRootCauseInstanceOf(IllegalArgumentException.class)
        .hasRootCauseMessage("Invalid resource");
  }

  @Test
  void mutatesResource_withAsyncMutator_whenOtherException() {
    var admissionController =
        new AsyncAdmissionController<>((AsyncMutator<HasMetadata>) (resource,
            operation) -> CompletableFuture.supplyAsync(() -> {
              throw new IllegalArgumentException("Invalid resource");
            }));

    admissionTestSupport.assertThatThrownBy(
        res -> admissionController.handle(res).toCompletableFuture()
            .join())
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(IllegalStateException.class)
        .hasRootCauseInstanceOf(IllegalArgumentException.class)
        .hasRootCauseMessage("Invalid resource");
  }
}
