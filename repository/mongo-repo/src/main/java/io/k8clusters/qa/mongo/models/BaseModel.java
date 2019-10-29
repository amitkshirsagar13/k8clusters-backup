package io.k8clusters.qa.mongo.models;

import org.springframework.data.annotation.Id;

public class BaseModel {

    @Id
    public String id;

    private Integer creationDate;
    private Integer lastModifiedDate;

    private String owner;
    private String ownerOrganization;
}
