package com.nordcloud.handson.kafka.domain.converter;

import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;
import com.nordcloud.handson.kafka.domain.model.UserTransaction;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class UserTransactionMessageConverter implements BiFunction<String, AddUserTransactionMessage, UserTransaction> {

    @Override
    public UserTransaction apply(String uuid, AddUserTransactionMessage addUserTransactionMessage) {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setTransactionUUID(uuid);
        userTransaction.setUsername(addUserTransactionMessage.getUsername());
        userTransaction.setTransactionType(addUserTransactionMessage.getTransactionType());
        return userTransaction;
    }
}
