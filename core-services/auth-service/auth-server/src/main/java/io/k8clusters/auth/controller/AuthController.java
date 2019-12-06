package io.k8clusters.auth.controller;

import io.k8clusters.auth.api.AuthorizationApi;
import io.k8clusters.auth.dto.AuthToken;
import io.k8clusters.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * AuthController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/6/2019
 */
@RestController
@RequestMapping("/")
public class AuthController implements AuthorizationApi {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<AuthToken> authToken(@NotNull @Valid String code) {
        return ResponseEntity.ok(authService.getAccessTokenFromGrantCode(code));
    }
}
