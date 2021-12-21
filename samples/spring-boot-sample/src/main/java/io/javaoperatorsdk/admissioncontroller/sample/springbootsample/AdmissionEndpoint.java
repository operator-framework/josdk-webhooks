package io.javaoperatorsdk.admissioncontroller.sample.springbootsample;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdmissionEndpoint {

    private final AdmissionController<Pod> addLabelMutationController;
    private final AdmissionController<Pod> validatingController;

    @Autowired
    public AdmissionEndpoint(@Qualifier("mutatingController") AdmissionController<Pod> addLabelMutationController,
                             @Qualifier("validatingController") AdmissionController<Pod> validatingController) {
        this.addLabelMutationController = addLabelMutationController;
        this.validatingController = validatingController;
    }

    @PostMapping("/mutate")
    @ResponseBody
    public ResponseEntity<AdmissionReview> mutate(@RequestBody AdmissionReview admissionReview) {
        AdmissionReview resultReview = addLabelMutationController.handle(admissionReview);
        return new ResponseEntity<>(resultReview, HttpStatus.valueOf(resultReview.getResponse().getStatus().getCode()));
    }

    @PostMapping("/validate")
    public ResponseEntity<AdmissionReview> validate(@RequestBody AdmissionReview admissionReview) {
        AdmissionReview resultReview = validatingController.handle(admissionReview);
        return new ResponseEntity<>(resultReview, HttpStatus.valueOf(resultReview.getResponse().getStatus().getCode()));
    }

}
