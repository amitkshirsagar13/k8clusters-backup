package io.k8clusters.auth.models;

import io.k8clusters.auth.repo.models.BaseModelAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Role:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
@Entity(name="ROLE")
public class Role extends BaseModelAudit {
    private String name;
}
