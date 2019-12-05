package io.k8clusters.auth.controller;

import io.k8clusters.auth.recreatedb.ImportXmlService;
import io.k8clusters.base.api.AdminApi;
import io.k8clusters.base.dto.Base;
import io.k8clusters.base.dto.XmlImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AdminController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */

@RestController
@RequestMapping("/api")
public class AdminController implements AdminApi {
    @Autowired
    private ImportXmlService importXmlService;

    @Override
    public ResponseEntity<XmlImporter> importXml(String mode) {
        XmlImporter xmlImporter = importXmlService.xmlImport(mode);
        return ResponseEntity.ok(xmlImporter);
    }
}
