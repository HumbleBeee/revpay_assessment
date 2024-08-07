package com.example.revpay.service;

import com.example.revpay.dto.TransactionDTO;

import java.math.BigDecimal;

public interface TransactionService {
    public void initiateTransaction(TransactionDTO transactionDTO);
    public BigDecimal getBalance(Long accountId);
}
