package io.javaoperatorsdk.admissioncontroller.sample.springbootsample;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.admissioncontroller.AdmissionController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public AdmissionController<HasMetadata> admissionController() {
        return new AdmissionController<>((resource, operation) -> {
            resource.getMetadata().getLabels().putIfAbsent("app.kubernetes.io/name", "mutation-test");
            return resource;
        });
    }

}
