package io.k8clusters.base.service;

import io.k8clusters.base.dto.Base;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    public Base getBase(String id) {
        Base base = new Base();
        base.setId(id);
        return base;
    }

}
