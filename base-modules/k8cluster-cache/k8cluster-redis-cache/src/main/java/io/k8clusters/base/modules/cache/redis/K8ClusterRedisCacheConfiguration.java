package io.k8clusters.base.modules.cache.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * K8ClusterRedisCacheConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class K8ClusterRedisCacheConfiguration {
    private boolean usePooling;
    private String redisHost;
    private int redisPort;
    private String password;
    public int maxRedirects;
    private String nodes;

}
