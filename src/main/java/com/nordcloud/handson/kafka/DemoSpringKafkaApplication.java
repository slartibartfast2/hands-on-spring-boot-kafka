package com.nordcloud.handson.kafka;

import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;
import com.nordcloud.handson.kafka.infrastructure.kafka.KafkaUserTransactionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.UUID;

@ConfigurationPropertiesScan
@RequiredArgsConstructor
@SpringBootApplication
public class DemoSpringKafkaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringKafkaApplication.class, args);
    }

    private final KafkaUserTransactionProducer userTransactionProducer;

    @Override
    public void run(String... strings) {

        for (int i = 1; i < 30; i++){
            String uuid = UUID.randomUUID().toString();
            AddUserTransactionMessage userTransactionMessage = AddUserTransactionMessage.builder()
                    .transactionType("LOGIN")
                    .username("akira.kurosawa" + i)
                    .build();

            userTransactionProducer.sendTransaction(uuid, userTransactionMessage);
        }
    }

}
