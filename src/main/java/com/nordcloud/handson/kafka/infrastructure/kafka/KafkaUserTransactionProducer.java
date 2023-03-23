package com.nordcloud.handson.kafka.infrastructure.kafka;


import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaUserTransactionProducer {

    private final KafkaTemplate<String, AddUserTransactionMessage> userTransactionKafkaTemplate;

    @Value("${app.kafka.topic.default-name}")
    private String topicName;

    public void sendTransaction(String uuid, AddUserTransactionMessage userTransactionMessage){
        log.info("Sending message='{}' to topic='{}'", userTransactionMessage, topicName);
        userTransactionKafkaTemplate.send(topicName,
                uuid,
                userTransactionMessage);
    }

}