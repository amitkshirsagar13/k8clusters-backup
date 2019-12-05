package io.k8clusters.base.exceptions;

/**
 * ERRORCODE:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
public enum ERRORCODE {
    GENERAL("0100"),
    API_UNAUTHORIZED("0401"),
    FORBIDDEN_AUTH_METHOD("0402"),
    ACCESS_DENIED("0403"),
    ACCESS_DENIED_FOR_INVITE_USER("0404"),
    SEND_MAIL_FAILURE("0405"),
    ACCESS_PROCESSING_EXCEPTION("0406");

    private final String code;

    ERRORCODE(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }
}
