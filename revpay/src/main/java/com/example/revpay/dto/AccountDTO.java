package com.example.revpay.dto;

import com.example.revpay.model.Account;
import com.example.revpay.model.Business;
import com.example.revpay.model.enums.ActivationStatus;
import com.example.revpay.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private Long id;
    private Long businessId;
    private Long bankAccountNumber;
    private String ifscCode;
    private ActivationStatus activationStatus;
    private BigDecimal balance;
    private TransactionType transactionType;

    public static AccountDTO fromEntity(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .businessId(account.getBusiness().getId())
                .bankAccountNumber(account.getBankAccountNumber())
                .ifscCode(account.getIfscCode())
                .activationStatus(account.getActivationStatus())
                .balance(account.getBalance())
                .transactionType(account.getTransactionType())
                .build();
    }

    public Account toEntity(Business business) {
        return Account.builder()
                .business(business)
                .bankAccountNumber(bankAccountNumber)
                .ifscCode(ifscCode)
                .activationStatus(activationStatus)
                .balance(balance)
                .transactionType(transactionType)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
