package io.k8clusters.auth.service;

import io.k8clusters.auth.properties.AuthServiceProperties;
import io.k8clusters.auth.repo.repository.IdMapperRepository;
import io.k8clusters.base.creators.Creator;
import io.k8clusters.base.dto.XmlImporter;
import io.k8clusters.base.services.BaseImportXmlService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * ImportXmlService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
public class ImportXmlService extends BaseImportXmlService {
    private AuthServiceProperties authServiceProperties;
    private IdMapperRepository idMapperRepository;

    public ImportXmlService(Map<String, Creator> creatorMap, AuthServiceProperties authServiceProperties, IdMapperRepository idMapperRepository) {
        setCreatorMap(creatorMap);
        setAuthServiceProperties(authServiceProperties);
        setIdMapperRepository(idMapperRepository);
    }


    @Override
    public IdMapperRepository getIdMapperRepository() {
        return idMapperRepository;
    }

    public XmlImporter xmlImport(String mode){
//        checkXmlUserRoleAccess();
        ApplicationContext beans = new ClassPathXmlApplicationContext(authServiceProperties.getXmlFileName());
        return recreateDBWithMicroService(mode, beans);
    }

}
