package io.k8clusters.qa.controller;


import io.k8clusters.base.filters.CorrelationHeaderFilter;
import io.k8clusters.qa.api.QaListApi;
import io.k8clusters.qa.dto.QA;
import io.k8clusters.qa.service.QaListService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * QaController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */

@RestController
@RequestMapping("/api")
public class QaController implements QaListApi {
    @Autowired
    private QaListService qaListService;

    public ResponseEntity<List<QA>> getQaList(Integer qaCount) {
        return ResponseEntity.ok(qaListService.getQaListMocked(qaCount));
    }
}
