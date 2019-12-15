package io.k8clusters.base.modules.config.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * K8ClusterSwaggerProperties:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class K8ClusterSwaggerProperties {
    @Value("${server.port:8080}")
    String port;

    @Value("${server.hostname:localhost}")
    String hostname;

    @Value("${server.servlet.context-path:/}")
    String contextPath;

    private String title;
    private String description;
    private String version;
    private Contacts contacts = new Contacts();

}
