package io.k8clusters.base.modules.aws.dynamodb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.k8clusters.base.modules.aws.dynamodb.config.K8ClusterDynamoDBConfig;
import io.k8clusters.base.modules.aws.dynamodb.integrative.DynamoDBIntegrativeInitializer;
import io.k8clusters.base.modules.aws.dynamodb.providers.AmazonDynamoDBClientProvider;
import io.k8clusters.base.modules.aws.dynamodb.providers.AwsLocalDynamoDBProvider;
import io.k8clusters.base.modules.aws.dynamodb.providers.AwsRuntimeDynamoDBProvider;
import io.k8clusters.base.modules.aws.dynamodb.runtime.DynamoDBMapperRuntimeInitializer;
import io.k8clusters.base.modules.config.aws.K8ClusterAwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * DynamodbAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class DynamodbAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "k8cluster.modules.dynamodb")
    public K8ClusterDynamoDBConfig k8ClusterDynamoDBConfig() {
        return new K8ClusterDynamoDBConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    AmazonDynamoDBClientProvider amazonDynamoDBClientProvider(K8ClusterDynamoDBConfig config, AWSCredentialsProvider credentials, K8ClusterAwsProperties awsProperties) {
        if (config.isIntegrative()) {
            log.info("Initializing Integrative AmazonDynamoDB Client");
            return new AwsLocalDynamoDBProvider();
        } else {
            Objects.requireNonNull(config.getEnvironmentPrefix(), "'environmentPrefix' parameter must be specified in runtime mode.");
            log.info("Initializing Runtime AmazonDynamoDB Client");
            return new AwsRuntimeDynamoDBProvider(credentials, awsProperties);
        }
    }

    @Bean
    AmazonDynamoDB amazonDynamoDB(AmazonDynamoDBClientProvider provider, K8ClusterDynamoDBConfig config) {
        return provider.provide(config);
    }

    @Bean
    public DynamoDBMapper dynamoDBMapperRuntimeInitializer(K8ClusterDynamoDBConfig config, AmazonDynamoDB dynamoDB, AWSCredentialsProvider credentials, K8ClusterAwsProperties awsProperties) {
        return new DynamoDBMapperRuntimeInitializer(config, dynamoDB, credentials, awsProperties).initialize();
    }

    @Bean
    @ConditionalOnProperty(name = "k8cluster.modules.dynamodb.integrative", havingValue = "true")
    public DynamoDBIntegrativeInitializer dynamoDBIntegrativeInitializer(DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB, K8ClusterDynamoDBConfig config) {
        return new DynamoDBIntegrativeInitializer(amazonDynamoDB, dynamoDBMapper, config);
    }

}
