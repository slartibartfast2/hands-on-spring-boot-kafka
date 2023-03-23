package com.nordcloud.handson.kafka.domain.repository;

import com.nordcloud.handson.kafka.domain.model.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {
    Optional<UserTransaction> findByTransactionUUID(String transactionUUID);
}