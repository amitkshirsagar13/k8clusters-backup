package io.k8clusters.auth.repo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
public class BaseModelAudit extends BaseEntity {

    @Column(name = "creationDate")
    private Integer creationDate;
    @Column(name = "lastModifiedDate")
    private Integer lastModifiedDate;

}
