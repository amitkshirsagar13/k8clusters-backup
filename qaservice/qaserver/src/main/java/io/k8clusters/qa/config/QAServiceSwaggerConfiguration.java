package io.k8clusters.qa.config;

import io.k8clusters.base.config.SwaggerConfiguration;
import springfox.documentation.service.Contact;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
public class QAServiceSwaggerConfiguration extends SwaggerConfiguration {


}
