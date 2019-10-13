package io.k8clusters.repo.mongo;

import io.k8clusters.qa.dto.QuestionType;
import io.k8clusters.repo.mongo.models.Choice;
import io.k8clusters.repo.mongo.models.QaCollection;
import io.k8clusters.repo.mongo.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"io.k8clusters.repo.mongo.repository"})
public class MongoConfig {

    @Autowired
    private QaRepository qaRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                QaCollection qa = new QaCollection();
                qa.setqType(QuestionType.MULTTYPE);
                qa.setQuestion("How are you?");
                qa.setPoint(4);
                qa.setMaxSelection(2);

                Choice choice = null;
                for (int i = 0; i < 4; i++) {
                    choice = new Choice();
                    choice.setValue(String.format("Person %s", i));
                    choice.setHint(String.format("I am %s", choice.getValue()));
                    qa.getAnswerList().add(choice);
                }
                qaRepository.save(qa);
            }
        };
    }
}
