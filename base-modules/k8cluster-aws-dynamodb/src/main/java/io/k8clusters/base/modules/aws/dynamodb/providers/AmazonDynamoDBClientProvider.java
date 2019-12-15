package io.k8clusters.base.modules.aws.dynamodb.providers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.k8clusters.base.modules.aws.dynamodb.config.K8ClusterDynamoDBConfig;

/**
 * AmazonDynamoDBClientProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
public interface AmazonDynamoDBClientProvider {
    AmazonDynamoDB provide(K8ClusterDynamoDBConfig config);
}
