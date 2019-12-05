package io.k8clusters.auth.properties;

import io.k8clusters.base.properties.XmlImporterProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AuthServiceProperties:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
public class AuthServiceProperties extends XmlImporterProperties {
}
