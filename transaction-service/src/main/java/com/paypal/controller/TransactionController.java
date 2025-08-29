package com.paypal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.entity.Transaction;
import com.paypal.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	
	private final TransactionService transactionservice;
	
	public TransactionController(TransactionService transactionservice) {
		this.transactionservice=transactionservice;
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @RequestBody Transaction transaction){
	Transaction created=	transactionservice.createTransaction(transaction);
		return ResponseEntity.ok(created);
	}
	@GetMapping("/all")
	public List<Transaction> getAllTransaction(){
	return 	transactionservice.getAllTransactions();
	}
}
