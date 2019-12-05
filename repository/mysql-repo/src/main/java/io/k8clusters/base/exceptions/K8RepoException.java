package io.k8clusters.base.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * K8Exception:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@Setter
public class K8RepoException extends RuntimeException {
    private static final long serialVersionUID = 1233566564434832234L;

    public K8RepoException(Throwable e, String msg, Object... args) {
        super(String.format(msg, args).concat(" | origin: " + e.toString()), e);
    }

    public K8RepoException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
