package io.k8clusters.base.modules.config.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * SwaggerModuleAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class AwsEnvironmentModuleAutoConfiguration {

    @Bean
    @ConfigurationProperties("k8cluster.aws")
    @ConditionalOnMissingBean
    public K8ClusterAwsProperties k8clusterAwsProperties(){
        return new K8ClusterAwsProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public AWSCredentialsProvider awsCredentialsProvider(K8ClusterAwsProperties k8clusterAwsProperties){
        String credentialsClassName = Optional.ofNullable(k8clusterAwsProperties.getCredentialsClass())
                .orElse(ProfileCredentialsProvider.class.getName());
        try {
            Class<?> credentialsClass = Class.forName(credentialsClassName);
            log.info("AWS Credentials class is: '{}'", credentialsClass.getSimpleName());
            return (AWSCredentialsProvider) credentialsClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Failed to initialize AWS credentials, provided class unknown: '%s'", credentialsClassName));
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(String.format("Failed to initialize AWS credentials, provided class  '%s' could not be initialized", credentialsClassName));
        }
    }

}
