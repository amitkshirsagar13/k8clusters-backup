package io.k8clusters.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "swagger")
public abstract class SwaggerConfiguration implements WebMvcConfigurer {

    @Value("${server.port:8080}")
    String port;

    @Value("${server.hostname:/localhost}")
    String hostname;

    @Value("${server.servlet.context-path:/}")
    String contextPath;

    private String title;
    private String description;
    private String version;
    private Contacts contacts = new Contacts();

    @Autowired
    Environment environment;

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
                .forCodeGeneration(true).host(getHostname() + ":" + getPort());
    }

    protected ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
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
                .termsOfServiceUrl("http://" + getHostname() + ":" + getPort() + contextPath + "/v2/api-docs?group=k8clusters")
                .license("LICENSE")
                .licenseUrl("http://" + getHostname() + ":" + getPort() + contextPath + "/v2/api-docs?group=k8clusters");
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public ApiInfo apiInfo() {
        return apiInfoBuilder().title(getTitle()).description(getDescription()).version(getVersion()).contact(getContact()).build();
    }

    private Contact getContact() {
        Contact contact = new Contact(contacts.getName(), contacts.getUrl(), contacts.getEmail());
        return contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class Contacts {

        private String name;
        private String email;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
