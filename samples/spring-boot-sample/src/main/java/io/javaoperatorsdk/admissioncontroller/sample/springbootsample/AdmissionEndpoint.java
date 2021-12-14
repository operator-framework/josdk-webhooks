package io.javaoperatorsdk.admissioncontroller.sample.springbootsample;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.admission.v1.AdmissionReview;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdmissionEndpoint {

    private final AdmissionController<HasMetadata> addLabelMutationController;

    @Autowired
    public AdmissionEndpoint(AdmissionController<HasMetadata> addLabelMutationController) {
        this.addLabelMutationController = addLabelMutationController;
    }

    @PostMapping("/mutate")
    @ResponseBody
    public AdmissionReview admissionReview(@RequestBody AdmissionReview admissionReview) {
        return addLabelMutationController.handle(admissionReview);
    }

}
