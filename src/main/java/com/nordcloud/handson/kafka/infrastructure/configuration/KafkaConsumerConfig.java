package com.nordcloud.handson.kafka.infrastructure.configuration;

import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.net.SocketTimeoutException;

@Slf4j
@RequiredArgsConstructor
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, AddUserTransactionMessage> kafkaTemplate;
    private final AppKafkaProperties appKafkaProperties;

    @Bean
    public ConsumerFactory<String, AddUserTransactionMessage> userTransactionConsumerFactory() {
        final var jsonDeserializer = new JsonDeserializer<AddUserTransactionMessage>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AddUserTransactionMessage> userTransactionKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, AddUserTransactionMessage>();
        factory.setConsumerFactory(userTransactionConsumerFactory());
        factory.setBatchListener(true);
        factory.setCommonErrorHandler(errorHandler(appKafkaProperties, kafkaTemplate));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(
            AppKafkaProperties properties,
            KafkaTemplate<String, AddUserTransactionMessage> kafkaTemplate) {
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (cr, e) -> {
                    log.error("consumed record {} because this exception was thrown: {}", cr.toString(), ExceptionUtils.getRootCauseMessage(e));
                    return new TopicPartition(cr.topic() + properties.deadletter().suffix(), 0);
        });

        Backoff backoff = properties.backoff();
        var exponentialBackOff = new ExponentialBackOffWithMaxRetries(backoff.maxRetries());
        exponentialBackOff.setInitialInterval(backoff.initialInterval().toMillis());
        exponentialBackOff.setMultiplier(backoff.multiplier());
        exponentialBackOff.setMaxInterval(backoff.maxInterval().toMillis());

        var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
        errorHandler.addRetryableExceptions(SocketTimeoutException.class, RuntimeException.class);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);

        return errorHandler;
    }
}
