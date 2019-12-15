package io.k8clusters.base.modules.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import io.k8clusters.base.modules.aws.dynamodb.integrative.DynamoDBIntegrativeInitializer;
import io.k8clusters.base.modules.test.core.MessageRepository;
import io.k8clusters.base.modules.test.core.MessageService;
import io.k8clusters.base.modules.test.model.DynamoDBMessage;
import io.k8clusters.base.modules.test.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * TestApplication:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@EnableCaching
@EnableJpaRepositories(basePackages = { "io.k8clusters.base.modules.test.core" })
@EntityScan(basePackages = {"io.k8clusters.base.modules.test.model"})
@SpringBootApplication
@EnableSwagger2
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Bean
    public MessageService messageService(MessageRepository messageRepository) {
        return new MessageService(messageRepository, dynamoDBMapper);
    }
    @Autowired
    private DynamoDBIntegrativeInitializer dynamoDBIntegrativeInitializer;

    @Override
    public void run(String... args) throws Exception {
        dynamoDBIntegrativeInitializer.doCreation();

        Message message = new Message();
        message.setId("id");
        message.setMessage("Hello There!!!");
        message.setTags("place");
        message.setError("Not Worth!!!");
        messageService(messageRepository).addMessage(message);

        message = new Message();
        message.setId("id1");
        message.setMessage("Hello Poonam!!!");
        message.setTags("love");
        message.setError("Worth!!!");
        messageService(messageRepository).addMessage(message);

        message = new Message();
        message.setId("id2");
        message.setMessage("Hello Amit!!!");
        message.setTags("life");
        message.setError("Bold!!!");
        messageService(messageRepository).addMessage(message);
        messageRepository.findAll().stream().forEach(System.out::println);

        DynamoDBMessage msg = new DynamoDBMessage();
        msg.setId("1357");
        msg.setMessage("{'name':'Amit Kshirsagar', 'message':'Hello World!!!'}");
        dynamoDBMapper.save(msg);
        msg.setId("7531");
        msg.setMessage("{'name':'Poonam Kshirsagar', 'message':'Hello Love!!!'}");
        dynamoDBMapper.save(msg);
        msg.setId("12357");
        msg.setMessage("{'name':'Amogh Kshirsagar', 'message':'Hello Future!!!'}");
        dynamoDBMapper.save(msg);

        List<DynamoDBMessage> dynamoDBMessages = dynamoDBMapper.scan(DynamoDBMessage.class, new DynamoDBScanExpression());
        dynamoDBMessages.stream().forEach(System.out::println);
    }

}
