package io.k8clusters.base.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

/**
 * JWTUtils:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @since May 1, 2019
 */
@Component
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;
    private static Algorithm algorithm = Algorithm.HMAC256("secret");

    public String buildJwt() {
        String token = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")) {
            token = getTokenString();
        }
        token = getTokenString();
        return token;
    }

    private String getTokenString() {
        String token;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        long expiryAt = Instant.now().getEpochSecond() + JWT_TOKEN_VALIDITY;
        JWTCreator.Builder jwtBuilder = JWT.create();
        jwtBuilder.withSubject("k8cluster.io")
                .withExpiresAt(new Date(expiryAt))
                .withClaim("aud","k8cluster.io")
                .withClaim("GivenName", authentication.getName())
                .withClaim("Surname", authentication.getName())
                .withClaim("Email", authentication.getName())
                .withClaim("username", authentication.getName())
                .withClaim("sub", authentication.getName());
        List<String> roleClaimList = new ArrayList<>();
        authorities.stream().forEach(authority -> {
            roleClaimList.add(authority.getAuthority());
        });
        jwtBuilder.withArrayClaim("ROLE", roleClaimList.stream().toArray(String[] ::new));
        token = jwtBuilder.sign(algorithm);
        return token;
    }

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
