package io.k8clusters.auth.creators;

import io.k8clusters.auth.models.User;
import io.k8clusters.auth.repo.models.BaseEntity;
import io.k8clusters.auth.repo.repository.IdMapperRepository;
import io.k8clusters.auth.repo.repository.UserRepository;
import io.k8clusters.base.creators.Creator;
import lombok.Getter;
import lombok.Setter;

/**
 * UserCreator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
public class UserCreator extends Creator {

    private UserRepository userRepository;
    public UserCreator(IdMapperRepository idMapperRepository, UserRepository userRepository) {
        setIdMapperRepository(idMapperRepository);
        setUserRepository(userRepository);
    }

    @Override
    public BaseEntity createOrUpdate(BaseEntity be) {
        BaseEntity rtn = null;
        if(isNotExisting(be)) {
            rtn = userRepository.save((User) be);
            be.setId(rtn.getId());
        } else {
            rtn = userRepository.findById(be.getId()).get();
            be.setId(rtn.getId());
            rtn = userRepository.save((User) be);
        }
        return rtn;
    }

    @Override
    public BaseEntity loadFromDb(BaseEntity be) {
        return null;
    }

    @Override
    public boolean prepared(BaseEntity be){
        boolean prepared = false;
        if (be != null) {
            prepared = !((User)be).getUserRoles().stream().anyMatch(role ->
                getIdMapperByExternalId(role.getExternalId())==null
            );
        }
        return prepared;
    }
}
