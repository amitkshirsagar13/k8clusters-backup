package io.k8clusters.base.modules.test.core;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.k8clusters.base.modules.test.model.DynamoDBMessage;
import io.k8clusters.base.modules.test.model.Message;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

/**
 * MessageService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
public class MessageService {
    private MessageRepository messageRepository;
    private DynamoDBMapper dynamoDBMapper;

    public MessageService(MessageRepository messageRepository, DynamoDBMapper dynamoDBMapper) {
        this.messageRepository = messageRepository;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Cacheable(value = "messages", key = "#messageId")
    public Optional<Message> getMessageById(String messageId) {
        System.out.println(String.format("MessageService.getMessageById: %s from repository", messageId));
        return messageRepository.findById(messageId);
    }

    public Optional<DynamoDBMessage> getDynamoMessageById(String messageId) {
        System.out.println(String.format("MessageService.getDynamoMessageById: %s from repository", messageId));
        return Optional.of(dynamoDBMapper.load(DynamoDBMessage.class, messageId));
    }

    @Cacheable(value = "messages", unless = "#result.id!=null")
    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }
}
