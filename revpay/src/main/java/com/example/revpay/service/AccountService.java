package com.example.revpay.service;

import com.example.revpay.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    public AccountDTO createAccount(AccountDTO request);
    public AccountDTO updateAccount(Long accountId, AccountDTO request);
    public void deleteAccount(Long accountId);
    public AccountDTO getAccountById(Long accountId);
    public List<AccountDTO> getAccountsByBusinessId(Long businessId);

}
