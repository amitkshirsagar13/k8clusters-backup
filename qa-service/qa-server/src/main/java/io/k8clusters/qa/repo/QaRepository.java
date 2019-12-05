package io.k8clusters.qa.repo;

import io.k8clusters.qa.repo.mongo.models.QaCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaRepository extends MongoRepository<QaCollection, String> {

}
