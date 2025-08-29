package com.paypal.service;

import java.util.List;

import com.paypal.entity.Transaction;

public interface TransactionService {

 Transaction createTransaction(Transaction transaction);
 List<Transaction> getAllTransactions();
}
