package io.k8clusters.base.modules.test.core;

import io.k8clusters.base.modules.test.model.DynamoDBMessage;
import io.k8clusters.base.modules.test.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * MessageController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessageController {
    private MessageService messageService;
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/sql/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<Message> getSqlMessage(@PathVariable String messageId) {
        log.info("Getting user with ID {} from Sql.", messageId);
        Optional<Message> messageById = messageService.getMessageById(messageId);
        return ResponseEntity.ok(messageById.isPresent()? messageById.get():null);
    }

    @RequestMapping(value = "/dynamo/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<DynamoDBMessage> getDynamoDbMessage(@PathVariable String messageId) {
        log.info("Getting user with ID {} from DynamoDb.", messageId);
        Optional<DynamoDBMessage> dynamoMessageById = messageService.getDynamoMessageById(messageId);
        return ResponseEntity.ok(dynamoMessageById.isPresent() ? dynamoMessageById.get():null);
    }
}
