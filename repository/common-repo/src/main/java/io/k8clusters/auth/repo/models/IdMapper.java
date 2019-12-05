package io.k8clusters.auth.repo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * IdMapper:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "K8CLUSTER_ID_MAPPER")
public class IdMapper {
    @Id
    @Column(name = "EXTERNAL_ID")
    public String externalId;
    @Column(name = "ENTITY_ID")
    public String id;
}
