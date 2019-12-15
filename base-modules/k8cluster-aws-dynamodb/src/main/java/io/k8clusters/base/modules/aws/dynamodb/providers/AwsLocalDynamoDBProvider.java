package io.k8clusters.base.modules.aws.dynamodb.providers;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import io.k8clusters.base.modules.aws.dynamodb.config.K8ClusterDynamoDBConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * AwsLocalDynamoDBProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
public class AwsLocalDynamoDBProvider implements AmazonDynamoDBClientProvider {
    @Override
    public AmazonDynamoDB provide(K8ClusterDynamoDBConfig config) {

        int httpPort = Optional.ofNullable(config.getDevelopment())
                .map(K8ClusterDynamoDBConfig.Development::getHttpPort)
                .orElseThrow(() -> new AmazonDynamoDBException("Define Http Port for the DynamoDB"));
        String hostname = Optional.ofNullable(config.getDevelopment())
                .map(K8ClusterDynamoDBConfig.Development::getHostName)
                .orElseThrow(() -> new AmazonDynamoDBException("Define HostName for the DynamoDB"));

        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(String.format("http://%s:%s", hostname, httpPort), "xxx-xx-xxx"))
                .build();

        log.info("Local Dynamo DB client was created at port '{}' .", httpPort);

        return dynamoDB;
    }
}
