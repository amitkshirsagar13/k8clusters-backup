package io.k8clusters.base.modules.test.core;

import io.k8clusters.base.modules.test.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MessageRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
}
