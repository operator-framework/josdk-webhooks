package io.javaoperatorsdk.webhook.admission;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.webhook.admission.mutation.AsyncMutator;

import static io.javaoperatorsdk.webhook.admission.Commons.*;

class AsyncAdmissionControllerTest {

  @Test
  void validatesResource() throws ExecutionException, InterruptedException {
    AsyncAdmissionController<HasMetadata> admissionController =
        new AsyncAdmissionController<>((resource, operation) -> {
          if (resource.getMetadata().getLabels().get(LABEL_KEY) == null) {
            throw new NotAllowedException(MISSING_REQUIRED_LABEL);
          }
        });
    var inputAdmissionReview = createTestAdmissionReview();

    CompletableFuture<AdmissionReview> response =
        (CompletableFuture<AdmissionReview>) admissionController.handle(inputAdmissionReview);

    assertValidation(response.get(), inputAdmissionReview.getRequest().getUid());
  }

  @Test
  void mutatesResource() throws ExecutionException, InterruptedException {
    AsyncAdmissionController<HasMetadata> admissionController =
        new AsyncAdmissionController<>((AsyncMutator<HasMetadata>) (resource,
            operation) -> CompletableFuture.supplyAsync(() -> {
              resource.getMetadata().getLabels().putIfAbsent(LABEL_KEY, LABEL_TEST_VALUE);
              return resource;
            }));
    var inputAdmissionReview = createTestAdmissionReview();

    CompletableFuture<AdmissionReview> response =
        (CompletableFuture<AdmissionReview>) admissionController.handle(inputAdmissionReview);

    assertMutation(response.get());
  }

}
