package io.k8clusters.base.controller;

import io.k8clusters.base.dto.JwtResponse;
import io.k8clusters.base.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/jwt")
    public ResponseEntity<JwtResponse> getJwt() {
        String token = jwtUtil.buildJwt();
        System.out.println(token);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

}
