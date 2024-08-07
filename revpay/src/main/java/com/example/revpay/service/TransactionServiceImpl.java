package com.example.revpay.service;

import com.example.revpay.dto.TransactionDTO;
import com.example.revpay.model.Account;
import com.example.revpay.model.Transaction;
import com.example.revpay.model.enums.ActivationStatus;
import com.example.revpay.model.enums.TransactionType;
import com.example.revpay.repository.AccountRepository;
import com.example.revpay.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private static final BigDecimal DAILY_LIMIT = BigDecimal.valueOf(20000);

    @Transactional
    public void initiateTransaction(TransactionDTO transactionDTO){
        try{
            Account sendersAccount = accountRepository.findById(transactionDTO.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender Account not Found"));

            Account receiverAccount = accountRepository.findById(transactionDTO.getBeneficiaryAccountId())
                            .orElseThrow(() -> new IllegalArgumentException("Receiver's account not found"));

            validateTransaction(transactionDTO, sendersAccount, receiverAccount);

            checkDailyLimit(sendersAccount, transactionDTO.getAmount());


            processCredit(receiverAccount, transactionDTO.getAmount());
            processDebit(sendersAccount, transactionDTO.getAmount());

        } catch (Exception e){
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }

    }

    private void validateTransaction(TransactionDTO transaction, Account sendersAccount, Account receiverAccount){

        if(transaction.getAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Transaction amount must be greater than 0");
        }

        if(transaction.getTransactionType().equals(TransactionType.DEBIT) && sendersAccount.getBalance().compareTo(transaction.getAmount()) < 0){
            throw new IllegalArgumentException("Insufficient balance for debit transaction");
        }

        if(sendersAccount.getActivationStatus().equals(ActivationStatus.INACTIVE)){
            throw new IllegalArgumentException("Please activate your account before making any transaction");
        }

        // Check if the sender account allows debit transactions
        if (!TransactionType.DEBIT.equals(sendersAccount.getTransactionType()) && !TransactionType.BOTH.equals(sendersAccount.getTransactionType())) {
            throw new IllegalArgumentException("Sender account is not set to allow debit transactions");
        }

        if (receiverAccount.getActivationStatus().equals(ActivationStatus.INACTIVE)) {
            throw new IllegalArgumentException("Beneficiary account is inactive");
        }

        // Check if the receiver account allows credit transactions
        if (!TransactionType.CREDIT.equals(receiverAccount.getTransactionType()) && !TransactionType.BOTH.equals(receiverAccount.getTransactionType())) {
            throw new IllegalArgumentException("Beneficiary account is not set to receive credit transactions");
        }
    }

    private void checkDailyLimit(Account account, BigDecimal transactionAmount){
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));

        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());

        List<Transaction> todayTransactions = transactionRepository.findByAccountIdAndCreatedAtAfter(account.getId(), startOfDay);

        BigDecimal totalToday = todayTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Check if adding the new transaction exceeds the daily limit
        if (totalToday.add(transactionAmount).compareTo(DAILY_LIMIT) > 0) {
            throw new IllegalArgumentException("Daily transaction limit exceeded");
        }
    }

    private void processCredit(Account senderAccount, BigDecimal amount){
        senderAccount.setBalance(senderAccount.getBalance().add(amount));
        accountRepository.save(senderAccount);

        Transaction transaction = Transaction.builder()
                .accountId(senderAccount.getId())
                .amount(amount)
                .transactionType("DEBIT")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        transactionRepository.save(transaction);
    }

    private void processDebit(Account senderAccount, BigDecimal amount){
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        Transaction transaction = Transaction.builder()
                .accountId(senderAccount.getId())
                .amount(amount)
                .transactionType("CREDIT")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        transactionRepository.save(transaction);
    }

    public BigDecimal getBalance(Long accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return account.getBalance();
    }

}
