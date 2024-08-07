package com.example.revpay.model;

import com.example.revpay.model.enums.ActivationStatus;
import com.example.revpay.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "bank_account_number", unique = true)
    private Long bankAccountNumber;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "activation_status")
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType = TransactionType.BOTH;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
