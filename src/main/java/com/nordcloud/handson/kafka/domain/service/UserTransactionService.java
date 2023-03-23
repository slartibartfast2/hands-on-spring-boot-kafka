package com.nordcloud.handson.kafka.domain.service;

import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;
import com.nordcloud.handson.kafka.domain.converter.UserTransactionMessageConverter;
import com.nordcloud.handson.kafka.domain.model.UserTransaction;
import com.nordcloud.handson.kafka.domain.repository.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserTransactionService {

    private final UserTransactionMessageConverter userTransactionMessageConverter;
    private final UserTransactionRepository userTransactionRepository;

    public void saveUserTransaction(String idempotencyKey, AddUserTransactionMessage addUserTransactionMessage) {
        Optional<UserTransaction> userTransactionOptional = userTransactionRepository.findByTransactionUUID(idempotencyKey);
        UserTransaction userTransaction;
        if(userTransactionOptional.isPresent()) {
            userTransaction = userTransactionOptional.get();
            log.info("UserTransactionMessage already saved to DB");
        } else {
            userTransaction = userTransactionMessageConverter.apply(idempotencyKey, addUserTransactionMessage);
            log.info("AddUserTransactionMessage converted to userTransaction");
            userTransactionRepository.save(userTransaction);
        }
        log.info("UserTransaction {} inserted successfully", userTransaction.getId());
    }
}
