package io.k8clusters.base.controller;

import io.k8clusters.base.api.BaseApi;
import io.k8clusters.base.dto.Base;
import io.k8clusters.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BaseController implements BaseApi {

    @Autowired
    private BaseService baseService;

    @Override
    public ResponseEntity<Base> getBaseID() {
        return ResponseEntity.ok(baseService.getBase("x83nss84n"));
    }
}
