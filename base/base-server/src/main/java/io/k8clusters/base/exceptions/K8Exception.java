package io.k8clusters.base.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * K8Exception:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Getter
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="k8Exception")
public class K8Exception extends RuntimeException {
    private static final long serialVersionUID = 1233566564434832234L;
    private ERRORCODE errorCode;

    public K8Exception(Throwable e, ERRORCODE errorId, String msg, Object... args) {
        super(String.format(msg, args).concat(" | origin: " + e.toString()), e);
        errorCode = errorId;
    }

    public K8Exception(ERRORCODE errorId, String msg, Object... args) {
        super(String.format(msg, args));
        errorCode = errorId;
    }
}
