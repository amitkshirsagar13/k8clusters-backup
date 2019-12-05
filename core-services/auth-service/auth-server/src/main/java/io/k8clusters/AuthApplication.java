package io.k8clusters;

import io.k8clusters.auth.creators.RoleCreator;
import io.k8clusters.auth.creators.UserCreator;
import io.k8clusters.auth.models.Role;
import io.k8clusters.auth.models.User;
import io.k8clusters.auth.properties.AuthServiceProperties;
import io.k8clusters.auth.recreatedb.ImportXmlService;
import io.k8clusters.auth.repo.repository.IdMapperRepository;
import io.k8clusters.auth.repo.repository.RoleRepository;
import io.k8clusters.auth.repo.repository.UserRepository;
import io.k8clusters.base.creators.Creator;
import io.k8clusters.base.service.BaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories("io.k8clusters")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("k8clusters.auth")
    public AuthServiceProperties authServiceProperties(){
        return new AuthServiceProperties();
    }

    @Bean
    public BaseService baseService() {
        return new BaseService();
    }

    @Bean
    public UserCreator userCreator(IdMapperRepository idMapperRepository, UserRepository userRepository) {
        return new UserCreator(idMapperRepository, userRepository);
    }

    @Bean
    public RoleCreator roleCreator(IdMapperRepository idMapperRepository, RoleRepository roleRepository) {
        return new RoleCreator(idMapperRepository, roleRepository);
    }

    @Bean
    public Map<String, Creator> creatorMap(UserCreator userCreator, RoleCreator roleCreator) {
        Map<String, Creator> creatorMap = new HashMap<>();
        creatorMap.put(User.class.getSimpleName(), userCreator);
        creatorMap.put(Role.class.getSimpleName(), roleCreator);
        return creatorMap;
    }

    @Bean
    public ImportXmlService importXmlService(AuthServiceProperties authServiceProperties, Map<String, Creator> creatorMap, IdMapperRepository idMapperRepository) {
        return new ImportXmlService(creatorMap, authServiceProperties, idMapperRepository);
    }
}
