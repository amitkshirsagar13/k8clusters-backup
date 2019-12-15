package io.k8clusters.base.modules.aws.dynamodb.integrative;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.util.StringUtils;
import io.k8clusters.base.modules.aws.dynamodb.config.K8ClusterDynamoDBConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.EventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.LinkedList;
import java.util.List;

/**
 * DynamoDBIntegrativeInitializer:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@RequiredArgsConstructor
public class DynamoDBIntegrativeInitializer {
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;
    private final K8ClusterDynamoDBConfig k8ClusterDynamoDBConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void createTablesForDev() {
        doCreation();
    }

    public void doCreation() {
        if (StringUtils.isNullOrEmpty(k8ClusterDynamoDBConfig.getDevelopment().getModelsPackage()))
            throw new RuntimeException("Package name must be specified in order to create the tables for integrative Dynamo DB");

        List<String>  tables = null;
        try {
            tables = dynamoDB.listTables().getTableNames();
        } catch (Exception e) {
            log.error("Failed to list tables from integrative dynamoDB, check if integrative dynamoDB server is running:  {}" ,e.toString());
            throw new RuntimeException(String.format("Failed to list tables from integrative dynamoDB, check if integrative dynamoDB server is running:  {}" ,e.toString()));
        }

        for (Class<?> model : findAllConfigurationClassesInPackage(k8ClusterDynamoDBConfig.getDevelopment().getModelsPackage())) {
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(model);

            if (tables.contains(tableRequest.getTableName())) {
                log.info("Table '{}' already created, Skipped table creation.", tableRequest.getTableName());
                continue;
            }

            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(15L, 10L));

            dynamoDB.createTable(tableRequest);
            log.info("{} table was created", tableRequest.getTableName());
        }
    }

    private List<Class<?>> findAllConfigurationClassesInPackage(String packageName) {
        List<Class<?>> annotatedClasses = new LinkedList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AnnotationTypeFilter(DynamoDBTable.class));
        for (BeanDefinition beanDefinition : provider.findCandidateComponents(packageName)) {
            try {
                annotatedClasses.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                log.warn("Could not resolve class object for bean definition", e);
            }
        }
        return annotatedClasses;
    }
}
