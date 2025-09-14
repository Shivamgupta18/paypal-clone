package com.paypal.service;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.kafka.KafkaEventProducer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.entity.Transaction;
import com.paypal.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepo;
	private final ObjectMapper objectmapper;
    private final KafkaEventProducer kafkaEventProducer;

	public TransactionServiceImpl(TransactionRepository transactionRepo, ObjectMapper objectmapper,KafkaEventProducer kafkaEventProducer) {
		this.objectmapper = objectmapper;
		this.transactionRepo = transactionRepo;
        this.kafkaEventProducer= kafkaEventProducer;
	}

	@Override
	public Transaction createTransaction(Transaction request) {
		// TODO Auto-generated method stub
		Long senderId = request.getSenderId();
		Long receiverId = request.getReceiverId();
		Double amount = request.getAmount();

		Transaction transaction = new Transaction();
		transaction.setReceiverId(receiverId);
		transaction.setSenderId(senderId);
		transaction.setAmount(amount);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setStatus("success");
        System.out.println("Incoming Transaction: " + transaction);

		Transaction saved= transactionRepo.save(transaction);
        System.out.println("Transaction created: " + saved);
        try {
            //Transaction eventPayload=objectmapper.writeValueAsString(saved);
            String key=String.valueOf(saved.getId());
            kafkaEventProducer.sendTransactionEvent(key, saved);
            System.out.println("Kafka message sent to transaction: : " );
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return saved;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		return transactionRepo.findAll();
	}

}
