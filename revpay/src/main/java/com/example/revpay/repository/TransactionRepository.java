package com.example.revpay.repository;

import com.example.revpay.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountIdAndCreatedAtAfter(Long accountId, Timestamp createdAt);
}
