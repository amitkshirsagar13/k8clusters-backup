package io.k8clusters.base.utils;


import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import io.k8clusters.base.properties.AuthServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;

/**
 * JWTUtils:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 *
 * @since May 1, 2019
 */
public class JwtUtil implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;
    private static Algorithm algorithm = Algorithm.HMAC256("secret");

    public static AuthServiceProperties authServiceProperties;
    public JwtUtil(AuthServiceProperties authServiceProperties) {
        this.authServiceProperties = authServiceProperties;
    }

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
            if (claims.size() == 0) {
                Algorithm algorithm = Algorithm.RSA256(keyProvider.getPublicKeyById(jwt.getKeyId()), null);
                JWTVerifier verifier = null;

                verifier = JWT.require(algorithm)
                        .withIssuer(authServiceProperties.getIssuer())
                        .withAudience(authServiceProperties.getClientId())
                        .acceptExpiresAt(604800)
                        .build();

                jwt = verifier.verify(token);
            }

            jwt.getClaims().forEach((k, c) -> claims.put(k, c.asString()));
        } catch (TokenExpiredException tokenExpiredException) {
            logger.error("Expired Token", tokenExpiredException);
        } catch (JWTDecodeException jwtDecodeException) {
            logger.error("JWTDecodeException", jwtDecodeException);
        } catch (Exception exception){
            logger.error(exception.getMessage(), exception);
        }
        return claims;
    }



    private static RSAKeyProvider keyProvider = new RSAKeyProvider() {
        private Map<String, RSAPublicKey> publicKeys = new HashMap<>();
        private Map<String, Long> lastModified = new HashMap<>();
        private long expiration = 3600000;

        @Override
        public RSAPublicKey getPublicKeyById(String kid) {
            RSAPublicKey key = publicKeys.get(kid);
            Long keyLastModified = lastModified.get(kid);
            long now = Instant.now().toEpochMilli();
            if (key == null || keyLastModified == null || keyLastModified + expiration < now) {
                key = requestPublicKey(kid);
                if (key != null) {
                    publicKeys.put(kid, key);
                    lastModified.put(kid, now);
                }
            }
            return key;
        }

        @SuppressWarnings("WeakerAccess")
        public RSAPublicKey requestPublicKey(String kid) {
            PublicKey publicKey = null; //Received 'kid' value might be null if it wasn't defined in the Token's header
            JwkProvider provider = new UrlJwkProvider(authServiceProperties.getDomain());
            try {
                Jwk jwk = provider.get(kid); //throws Exception when not found or can't get one
                publicKey = jwk.getPublicKey();
            } catch (JwkException e) {
            }
            return (RSAPublicKey) publicKey;
        }

        @Override
        public RSAPrivateKey getPrivateKey() {
            return null;
        }

        @Override
        public String getPrivateKeyId() {
            return null;
        }
    };


}
