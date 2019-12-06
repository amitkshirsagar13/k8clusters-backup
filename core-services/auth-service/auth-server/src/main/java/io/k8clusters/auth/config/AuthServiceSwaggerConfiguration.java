package io.k8clusters.auth.config;

import io.k8clusters.base.config.SwaggerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * AuthServiceSwaggerConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @since May 1, 2019
 */
@Component
public class AuthServiceSwaggerConfiguration extends SwaggerConfiguration {

    @Bean
    public Docket apiAuth() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("authService").select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/authorization/**")).build().apiInfo(apiInfo())
                .host(getHostname() + ":" + getPort());
    }
}
