package com.example.revpay.controller;

import com.example.revpay.dto.AccountDTO;
import com.example.revpay.service.AccountService;
import com.example.revpay.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO request) {
        AccountDTO response = accountService.createAccount(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long accountId, @RequestBody AccountDTO request) {
        AccountDTO response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long accountId) {
        AccountDTO response = accountService.getAccountById(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByBusinessId(@PathVariable Long businessId) {
        List<AccountDTO> response = accountService.getAccountsByBusinessId(businessId);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/{acocuntId}/transactions")
//    public ResponseEntity<String> initiateTransaction(@PathVariable Long accountId, @RequestBody TransactionDTO transactionDTO){
//
//    }
}
