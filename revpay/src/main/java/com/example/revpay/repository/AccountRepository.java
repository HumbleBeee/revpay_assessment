package com.example.revpay.repository;

import com.example.revpay.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByBusinessId(Long businessId);

    boolean existsByBankAccountNumber(Long bankAccountNumber);
}
