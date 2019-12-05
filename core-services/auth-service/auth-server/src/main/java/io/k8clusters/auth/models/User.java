package io.k8clusters.auth.models;

import io.k8clusters.auth.repo.models.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * User:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
@Entity
public class User extends BaseModel {
    @Column(unique = true)
    private String userName;
    private String email;
    private boolean active;
    private String organization;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> userRoles;

}
