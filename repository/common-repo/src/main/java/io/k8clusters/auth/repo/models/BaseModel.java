package io.k8clusters.auth.repo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * BaseModel:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
public class BaseModel extends BaseModelAudit {

    @Column(name = "owner")
    private String owner;
    @Column(name = "ownerOrganization")
    private String ownerOrganization;
}
