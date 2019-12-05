package io.k8clusters.auth.repo.repository;

import io.k8clusters.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * io.k8clusters.auth.repo.repository.UserRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
