package io.k8clusters.base.modules.config.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerModuleAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class SwaggerModuleAutoConfiguration {

    @Bean
    @ConfigurationProperties("k8cluster.swagger")
    public K8ClusterSwaggerProperties k8ClusterSwaggerProperties(){
        return new K8ClusterSwaggerProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public SwaggerConfiguration swaggerConfiguration(K8ClusterSwaggerProperties k8ClusterSwaggerProperties){
        return new SwaggerConfiguration(k8ClusterSwaggerProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket apiDocket(SwaggerConfiguration swaggerConfiguration){
        return swaggerConfiguration.api();
    }
}
