package io.javaoperatorsdk.admissioncontroller.sample.springbootsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;

@RestController
public class AdmissionEndpoint {

  private final AdmissionController<Pod> addLabelMutationController;
  private final AdmissionController<Pod> validatingController;

  @Autowired
  public AdmissionEndpoint(
      @Qualifier("mutatingController") AdmissionController<Pod> addLabelMutationController,
      @Qualifier("validatingController") AdmissionController<Pod> validatingController) {
    this.addLabelMutationController = addLabelMutationController;
    this.validatingController = validatingController;
  }

  @PostMapping("/mutate")
  @ResponseBody
  public AdmissionReview mutate(@RequestBody AdmissionReview admissionReview) {
    return addLabelMutationController.handle(admissionReview);
  }

  @PostMapping("/validate")
  @ResponseBody
  public AdmissionReview validate(@RequestBody AdmissionReview admissionReview) {
    return validatingController.handle(admissionReview);
  }

}
