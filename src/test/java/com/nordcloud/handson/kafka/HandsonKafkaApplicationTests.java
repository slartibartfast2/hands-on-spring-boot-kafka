package com.nordcloud.handson.kafka;

import com.nordcloud.handson.kafka.domain.repository.UserTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class HandsonKafkaApplicationTests {

    private static final String USERS_TRANSACTIONS = "users.transactions";
    private static final String USERS_TRANSACTIONS_DLT = "users.transactions.DLT";

    @Autowired
    private UserTransactionRepository repository;

    private static final Logger log = LoggerFactory.getLogger(HandsonKafkaApplicationTests.class);

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

}
