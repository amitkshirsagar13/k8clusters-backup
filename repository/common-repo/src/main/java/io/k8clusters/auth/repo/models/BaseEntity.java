package io.k8clusters.auth.repo.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * BaseEntity:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "io.k8clusters.auth.repo.models.UUIDGenerator")
    @GeneratedValue(generator = "UUIDGenerator")
    public String id;

    @Column(name = "isDeleted")
    private boolean deleted;

    @Transient
    private String externalId;

    @Transient
    public Set getDependents() {
        return null;
    }
}
