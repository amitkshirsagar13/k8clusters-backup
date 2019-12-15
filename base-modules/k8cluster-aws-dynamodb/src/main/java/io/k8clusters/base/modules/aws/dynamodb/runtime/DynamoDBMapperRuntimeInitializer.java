package io.k8clusters.base.modules.aws.dynamodb.runtime;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.AttributeEncryptor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.DynamoDBEncryptor;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.providers.DirectKmsMaterialProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.util.StringUtils;
import io.k8clusters.base.modules.aws.dynamodb.config.K8ClusterDynamoDBConfig;
import io.k8clusters.base.modules.config.aws.K8ClusterAwsProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

/**
 * DynamoDBMapperInitializer:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
public class DynamoDBMapperRuntimeInitializer {
    private final K8ClusterDynamoDBConfig k8ClusterDynamoDBConfig;
    private final AmazonDynamoDB awsDynamoDB;
    private final AWSCredentialsProvider awsCredentialsProvider;
    private final K8ClusterAwsProperties k8ClusterAwsProperties;

    public DynamoDBMapperRuntimeInitializer(K8ClusterDynamoDBConfig k8ClusterDynamoDBConfig, AmazonDynamoDB awsDynamoDB,
                                            AWSCredentialsProvider awsCredentialsProvider, K8ClusterAwsProperties k8ClusterAwsProperties) {
        this.k8ClusterDynamoDBConfig = k8ClusterDynamoDBConfig;
        this.awsDynamoDB = awsDynamoDB;
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.k8ClusterAwsProperties = k8ClusterAwsProperties;
    }

    public DynamoDBMapper initialize() {

        String envPrefix = Optional.ofNullable(k8ClusterDynamoDBConfig.getEnvironmentPrefix())
                .map(env -> env + "_")
                .orElse(null);

        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(k8ClusterDynamoDBConfig.getSaveBehavior())
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(envPrefix))
                .build();

        AttributeEncryptor encryptor = null;
        if (!StringUtils.isNullOrEmpty(k8ClusterDynamoDBConfig.getArn())) {
            Objects.requireNonNull(k8ClusterAwsProperties.getRegion(), "Parameter 'k8cluster.modules.dynamodb.region' must be specified with arn.");

            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withCredentials(awsCredentialsProvider)
                    .withRegion(k8ClusterAwsProperties.getRegion())
                    .build();

            encryptor = new AttributeEncryptor(DynamoDBEncryptor.getInstance(new DirectKmsMaterialProvider(kmsClient, k8ClusterDynamoDBConfig.getArn())));
            log.info("Encryption enabled with KMS for DynamoDBMapper");
        }else{
            log.info("Encryption disabled DynamoDBMapper");
        }

        return new DynamoDBMapper(awsDynamoDB, mapperConfig, encryptor);
    }
}
