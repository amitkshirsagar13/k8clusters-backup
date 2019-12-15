package io.k8clusters.base.modules.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */

public class SwaggerConfiguration implements WebMvcConfigurer {
    private K8ClusterSwaggerProperties k8ClusterSwaggerProperties;
    public SwaggerConfiguration(K8ClusterSwaggerProperties k8ClusterSwaggerProperties) {
        this.k8ClusterSwaggerProperties = k8ClusterSwaggerProperties;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("k8clusters").select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**")).build().apiInfo(apiInfo())
                .securitySchemes(newArrayList(apiKey())).securityContexts(newArrayList(securityContext()))
                .forCodeGeneration(true).host(k8ClusterSwaggerProperties.getHostname() + ":" + k8ClusterSwaggerProperties.getPort());
    }

    public ApiInfo apiInfo() {
        return apiInfoBuilder().title(k8ClusterSwaggerProperties.getTitle()).description(k8ClusterSwaggerProperties.getDescription()).version(k8ClusterSwaggerProperties.getVersion()).contact(getContact()).build();
    }

    protected ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private Contact getContact() {
        Contact contact = new Contact(k8ClusterSwaggerProperties.getContacts().getName(), k8ClusterSwaggerProperties.getContacts().getUrl(), k8ClusterSwaggerProperties.getContacts().getEmail());
        return contact;
    }

    protected SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant("/api/**"))
                .build();
    }

    protected <T> List<T> newArrayList(T something) {
        List<T> list = new ArrayList<>();
        list.add(something);
        return list;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }

    public ApiInfoBuilder apiInfoBuilder() {

        return new ApiInfoBuilder()
                .termsOfServiceUrl("http://" + k8ClusterSwaggerProperties.getHostname() + ":" + k8ClusterSwaggerProperties.getPort() + k8ClusterSwaggerProperties.getContextPath() + "/v2/api-docs?group=k8clusters")
                .license("LICENSE")
                .licenseUrl("http://" + k8ClusterSwaggerProperties.getHostname() + ":" + k8ClusterSwaggerProperties.getPort() + k8ClusterSwaggerProperties.getContextPath() + "/v2/api-docs?group=k8clusters");
    }
}
