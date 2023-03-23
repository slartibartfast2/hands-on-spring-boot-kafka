package com.nordcloud.handson.kafka.application.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddUserTransactionMessage {

    private String transactionType;
    private String username;
}
