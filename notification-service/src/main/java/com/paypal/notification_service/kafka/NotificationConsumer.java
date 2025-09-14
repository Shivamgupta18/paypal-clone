package com.paypal.notification_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paypal.notification_service.entity.Notification;
import com.paypal.entity.Transaction;
import com.paypal.notification_service.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    private final ObjectMapper mapper;

    public NotificationConsumer(NotificationRepository notificationRepository, ObjectMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @KafkaListener(topics = "txn-initiated" ,groupId = "notification-group")
    public void consumeTransaction(Transaction transaction) throws JsonProcessingException {
        Notification notification=new Notification();
        notification.setUserId(transaction.getReceiverId());
        System.out.println("Received transaction id "+transaction.getReceiverId());
        String notify= "$"+ transaction.getAmount()+ " received from "+ transaction.getSenderId();
        notification.setMessage(notify);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
        System.out.println("Notification saved to database: "+ notification);
    }
}
