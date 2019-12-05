package io.k8clusters;

import io.k8clusters.base.service.BaseService;
import io.k8clusters.qa.config.QAServiceSwaggerConfiguration;
import io.k8clusters.qa.repo.QaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * K8ClusterCryptoService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @since Apr 28, 2019
 */

@SpringBootApplication
//@EnableDiscoveryClient
@EnableMongoRepositories(basePackageClasses = QaRepository.class)
public class QaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QaApplication.class, args);
    }

    @Bean
    public BaseService baseService() {
        return new BaseService();
    }

    @Bean
    @ConfigurationProperties(prefix = "swagger")
    public QAServiceSwaggerConfiguration qaServiceSwaggerConfiguration() {
        return new QAServiceSwaggerConfiguration();
    }

}
