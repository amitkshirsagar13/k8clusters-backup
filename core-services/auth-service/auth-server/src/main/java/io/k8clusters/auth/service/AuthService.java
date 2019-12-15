package io.k8clusters.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.k8clusters.auth.dto.AuthToken;
import io.k8clusters.base.exceptions.ERRORCODE;
import io.k8clusters.base.exceptions.K8Exception;
import io.k8clusters.base.properties.AuthServiceProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/6/2019
 */
public class AuthService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private RestTemplate restTemplate;
    private AuthServiceProperties authServiceProperties;
    public AuthService(RestTemplate restTemplate, AuthServiceProperties authServiceProperties) {
        this.restTemplate = restTemplate;
        this.authServiceProperties = authServiceProperties;
    }
    //------------------------------------------------------------//
    // Exchange the given grant code with an access token (id token)
    //------------------------------------------------------------//
    public AuthToken getAccessTokenFromGrantCode(String grantCode) throws K8Exception {
        AuthToken authToken = new AuthToken();
        try {
            String response = requestAccessAndRefreshTokens(grantCode);
            JsonNode jsonObject =  mapper.readTree( response );
            String token = jsonObject.get("id_token").textValue();
            Map<String, String> claims = getClaimsWithAuthValidation(token);
            authToken.setAccessToken(token);
            authToken.setUserName(claims.get("name"));
        } catch (K8Exception e) {
            authToken.setError(e.getMessage());
            throw e;
        } catch (Exception e) {
            authToken.setError(e.getMessage());
            e.printStackTrace();
            throw new K8Exception(ERRORCODE.GENERAL, "Error while trying to exchange grant code with an access token");
        }
        return authToken;
    }

    public static Map<String, String> decodeTokenToClaimsMap(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, String> claims = new HashMap<>();
        jwt.getClaims().forEach((k, c) -> claims.put(k, c.asString()));
        return claims;
    }

    private Map<String, String> getClaimsWithAuthValidation(String token) {
        Map<String, String> userDetails = decodeTokenToClaimsMap(token);
        return userDetails;
    }


    private String requestAccessAndRefreshTokens(String grantCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", authServiceProperties.getClientId());
        map.add("client_secret", authServiceProperties.getClientSecret());
//        map.add("audience", authServiceProperties.getApiV2Audience());
        map.add("code", grantCode);
        map.add("redirect_uri", authServiceProperties.getCallbackUrl());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        String authServiceUrl = String.format("%s/oauth/token", authServiceProperties.getDomain());
        ResponseEntity<String> response = restTemplate.postForEntity( authServiceUrl, request , String.class );
        return response.getBody();
    }
}
