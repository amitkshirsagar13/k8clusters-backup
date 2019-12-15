package io.k8clusters.base.properties;

import lombok.Getter;
import lombok.Setter;

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
    private String apiV2Audience;
    private String callbackUrl;
    private String clientId;
    private String clientSecret;
    private String domain;
    private String issuer;
}
