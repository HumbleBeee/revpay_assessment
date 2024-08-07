package com.example.revpay.service;

import com.example.revpay.dto.AccountDTO;
import com.example.revpay.model.Account;
import com.example.revpay.model.Business;
import com.example.revpay.model.enums.TransactionType;
import com.example.revpay.repository.AccountRepository;
import com.example.revpay.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final BusinessRepository businessRepository;

    public AccountDTO createAccount(AccountDTO request){

        if (accountRepository.existsByBankAccountNumber(request.getBankAccountNumber())) {
            throw new IllegalArgumentException("Bank account number already exists.");
        }

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new IllegalArgumentException("Business not Found"));

        Account account = Account.builder()
                .business(business)
                .bankAccountNumber(request.getBankAccountNumber())
                .ifscCode(request.getIfscCode())
                .activationStatus(request.getActivationStatus())
                .balance(request.getBalance())
                .transactionType(Optional.ofNullable(request.getTransactionType()).orElse(TransactionType.BOTH))
                /*.transactionType(request.getTransactionType())*/
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        account = accountRepository.save(account);
        return AccountDTO.fromEntity(account);
    }

    public AccountDTO updateAccount(Long accountId, AccountDTO request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setBankAccountNumber(request.getBankAccountNumber());
        account.setIfscCode(request.getIfscCode());
        account.setActivationStatus(request.getActivationStatus());
        account.setBalance(request.getBalance());
        account.setTransactionType(request.getTransactionType());
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Account savedAccount = accountRepository.save(account);

        return AccountDTO.fromEntity(savedAccount);
    }

    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account not found");
        }

        accountRepository.deleteById(accountId);
    }

    public AccountDTO getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return AccountDTO.fromEntity(account);
    }

    public List<AccountDTO> getAccountsByBusinessId(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new IllegalArgumentException("Business not found");
        }

        List<Account> accounts = accountRepository.findByBusinessId(businessId);
        return accounts.stream()
                .map(AccountDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
