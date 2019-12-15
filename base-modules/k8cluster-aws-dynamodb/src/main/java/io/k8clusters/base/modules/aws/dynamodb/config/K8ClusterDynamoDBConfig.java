package io.k8clusters.base.modules.aws.dynamodb.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * K8ClusterDynamoDBConfig:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class K8ClusterDynamoDBConfig {

    private String arn;
    private String environmentPrefix;
    private boolean integrative;
    private Development development;
    private DynamoDBMapperConfig.SaveBehavior saveBehavior = DynamoDBMapperConfig.SaveBehavior.PUT;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Development {
        private String modelsPackage;
        private String hostName;
        private int httpPort = 8000;

    }
}
