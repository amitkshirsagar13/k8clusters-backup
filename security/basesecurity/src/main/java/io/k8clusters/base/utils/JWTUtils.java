package io.k8clusters.base.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.HashMap;
import java.util.Map;

/**
 * JWTUtils:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @since May 1, 2019
 */
public class JWTUtils {

    /**
     * @param token
     * @return
     */
    public static Map<String, String> getVerifiedClaims(String token) {
        Map<String, String> claims = new HashMap();
        try {
            DecodedJWT jwt = JWT.decode(token);
            for (String claim : jwt.getClaims().keySet()) {
                claims.put(claim, jwt.getClaims().get(claim).asString());
            }
        } catch (JWTDecodeException exception) {
            // Invalid token
        }
        return claims;
    }

}
