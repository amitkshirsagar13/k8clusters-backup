package io.k8clusters.auth.creators;

import io.k8clusters.auth.models.Role;
import io.k8clusters.auth.repo.models.BaseEntity;
import io.k8clusters.auth.repo.repository.IdMapperRepository;
import io.k8clusters.auth.repo.repository.RoleRepository;
import io.k8clusters.auth.repo.repository.UserRepository;
import io.k8clusters.base.creators.Creator;
import lombok.Getter;
import lombok.Setter;

/**
 * UserRoleCreator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
public class RoleCreator extends Creator {

    private RoleRepository roleRepository;
    public RoleCreator(IdMapperRepository idMapperRepository, RoleRepository roleRepository) {
        setIdMapperRepository(idMapperRepository);
        setRoleRepository(roleRepository);
    }

    @Override
    public BaseEntity createOrUpdate(BaseEntity be) {
        BaseEntity rtn = null;
        if(isNotExisting(be)) {
            rtn = roleRepository.save((Role) be);
            be.setId(rtn.getId());
        } else {
            rtn = roleRepository.findById(be.getId()).get();
            be.setId(rtn.getId());
            rtn = roleRepository.save((Role) be);
        }
        return rtn;
    }

    @Override
    public BaseEntity loadFromDb(BaseEntity be) {
        return null;
    }
}
