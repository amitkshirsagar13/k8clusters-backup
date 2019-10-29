package io.k8clusters.qa;

import io.k8clusters.qa.repo.QaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@ComponentScan(basePackages = { "io.k8clusters" })
@EnableMongoRepositories(basePackageClasses = QaRepository.class)

public class QaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QaApplication.class, args);
    }
}
