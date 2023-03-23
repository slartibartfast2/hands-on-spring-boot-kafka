package com.nordcloud.handson.kafka.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user_transactions")
public class UserTransaction {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "transaction_uuid")
    private String transactionUUID;

    @NotNull
    @Column(name = "transaction_type")
    private String transactionType;
    private String username;
}
