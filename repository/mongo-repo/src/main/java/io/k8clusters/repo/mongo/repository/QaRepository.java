package io.k8clusters.repo.mongo.repository;

import io.k8clusters.repo.mongo.models.QaCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QaRepository extends MongoRepository<QaCollection, String> {
}
