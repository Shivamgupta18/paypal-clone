package com.paypal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.entity.Transaction;
import com.paypal.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepo;
	private final ObjectMapper objectmapper;

	public TransactionServiceImpl(TransactionRepository transactionRepo, ObjectMapper objectmapper) {
		this.objectmapper = objectmapper;
		this.transactionRepo = transactionRepo;
	}

	@Override
	public Transaction createTransaction(Transaction request) {
		// TODO Auto-generated method stub
		String senderId = request.getSenderId();
		String receiverId = request.getReceiverId();
		Double amount = request.getAmount();

		Transaction transaction = new Transaction();
		transaction.setReceiverId(receiverId);
		transaction.setSenderId(senderId);
		transaction.setAmount(amount);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setStatus("success");
		Transaction created= transactionRepo.save(transaction);
		return created;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		return transactionRepo.findAll();
	}

}
