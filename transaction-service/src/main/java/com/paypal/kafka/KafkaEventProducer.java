package com.paypal.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paypal.entity.Transaction;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class KafkaEventProducer {
    private  static final String TOPIC = "txn-initiated";

    private final KafkaTemplate<String,Transaction> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Autowired
    public KafkaEventProducer(KafkaTemplate<String,Transaction> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void sendTransactionEvent(String key, Transaction transaction){
        System.out.println("Sending  to kafka-> Topic: " + TOPIC+ "key: " + key + " message: " + transaction);
        CompletableFuture< SendResult<String, Transaction>> future = kafkaTemplate.send(TOPIC, key, transaction);
        future.thenAccept(result -> {
            RecordMetadata metadata=result.getRecordMetadata();
          System.out.println("kafka message send successfully topic"+ metadata.topic()+ "Partitiion" +metadata.partition()+ "offset"+metadata.offset());
        }).exceptionally(ex-> {
            System.err.println("Failed to send kafka message "+ex.getMessage());
            return null;
        });
    }

}
