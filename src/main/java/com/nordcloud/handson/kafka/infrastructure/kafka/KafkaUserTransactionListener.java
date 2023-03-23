package com.nordcloud.handson.kafka.infrastructure.kafka;

import com.nordcloud.handson.kafka.application.listener.UserTransactionListener;
import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;
import com.nordcloud.handson.kafka.domain.service.UserTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaUserTransactionListener implements UserTransactionListener {

    private final UserTransactionService userTransactionService;

    @KafkaListener(groupId = "user-trx-batch", topics = "${app.kafka.topic.default-name}", containerFactory = "userTransactionKafkaListenerContainerFactory")
    public void processUserTransaction(@Payload List<AddUserTransactionMessage> payloadList,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys) {

        log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("Starting the process to receive batch messages, payload size: " + payloadList.size());

        for (int i = 0; i < payloadList.size(); i++) {
            log.info("Received message='{}' with partition-offset='{}' and key= '{}'", payloadList.get(i),
                    partitions.get(i) + "-" + offsets.get(i), keys.get(i));
            userTransactionService.saveUserTransaction(keys.get(i), payloadList.get(i));
        }
        log.info("all the batch messages are consumed");
    }

    @KafkaListener(groupId = "user-trx-batch", topics = "${app.kafka.topic.default-name}${app.kafka.deadletter.suffix}")
    public void processDLT(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.warn("received DLT message : [{}] from topic : [{}] at [{}]", message, topic, LocalDateTime.now());
    }
}