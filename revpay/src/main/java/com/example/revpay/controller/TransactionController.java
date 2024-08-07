package com.example.revpay.controller;

import com.example.revpay.dto.TransactionDTO;
import com.example.revpay.service.TransactionService;
import com.example.revpay.service.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<?> initiateTransaction(@PathVariable Long accountId, @RequestBody TransactionDTO transaction){
        transaction.setAccountId(accountId);
        transactionService.initiateTransaction(transaction);
        return ResponseEntity.ok("Transaction Successful");
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long accountId){
        BigDecimal balance = transactionService.getBalance(accountId);
        return ResponseEntity.ok(balance);
    }
}
