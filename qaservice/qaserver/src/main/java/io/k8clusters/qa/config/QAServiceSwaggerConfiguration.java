package io.k8clusters.qa.config;

import io.k8clusters.base.config.SwaggerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * CryptoSwaggerConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @since May 1, 2019
 */
@Component
public class QAServiceSwaggerConfiguration extends SwaggerConfiguration {
    @Override
    public ApiInfo apiInfo() {
        return apiInfoBuilder().title("k8clusters QaService").description("Swagger Documentation for the QaService APIs").version("3.0").build();
    }

}
