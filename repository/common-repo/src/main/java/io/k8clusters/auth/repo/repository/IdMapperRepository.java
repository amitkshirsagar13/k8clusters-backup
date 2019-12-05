package io.k8clusters.auth.repo.repository;

import io.k8clusters.auth.repo.models.IdMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * IdMapperRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Repository
public interface IdMapperRepository extends JpaRepository<IdMapper, String> {
    @Query
    IdMapper findByExternalId(String externalId);
}
