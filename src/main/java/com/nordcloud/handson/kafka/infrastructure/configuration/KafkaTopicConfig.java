package com.nordcloud.handson.kafka.infrastructure.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.topic.default-name}")
    private String topicName;

    @Bean
    public NewTopic userTransactionTopic() {
        return TopicBuilder.name(topicName)
                .partitions(4)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic deadLetterTopic(AppKafkaProperties properties) {
        return TopicBuilder.name(topicName + properties.deadletter().suffix())
                .partitions(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "" + properties.deadletter().retention().toMillis())
                .build();
    }
}
