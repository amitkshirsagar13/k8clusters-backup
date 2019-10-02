package io.k8clusters.config;

import io.k8clusters.base.config.SwaggerConfiguration;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiInfo;

@Component
public class TestSwaggerConfig extends SwaggerConfiguration {

    @Override
    public ApiInfo apiInfo() {
        return apiInfoBuilder().title("Base Test").description("Swagger Documentation for the Base APIs").version("3.0").build();
    }
}
