package io.k8clusters.base.modules.test.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Message:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Getter
@Setter
@Entity
public class Message implements Serializable {
    private static final long serialVersionUID = 7156526077883281625L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String message;
    private String error;
    private String tags;

    @Override
    public String toString() {
        return String.format("{'id': '%s', 'message': '%s'}", getId(), getMessage());
    }
}
