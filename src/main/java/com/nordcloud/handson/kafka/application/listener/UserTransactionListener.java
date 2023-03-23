package com.nordcloud.handson.kafka.application.listener;

import com.nordcloud.handson.kafka.application.model.AddUserTransactionMessage;

import java.util.List;

public interface UserTransactionListener {

    void processUserTransaction(List<AddUserTransactionMessage> payloadList,
                                List<Integer> partitions,
                                List<Long> offsets,
                                List<String> keys);
}
