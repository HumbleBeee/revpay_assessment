package com.example.revpay;

import com.example.revpay.dto.AccountDTO;
import com.example.revpay.model.Account;
import com.example.revpay.model.Business;
import com.example.revpay.model.enums.ActivationStatus;
import com.example.revpay.repository.AccountRepository;
import com.example.revpay.repository.BusinessRepository;
import com.example.revpay.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BusinessRepository businessRepository;

    private Business business;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        business = Business.builder()
                .id(1L)
                .name("Test Business")
                .email("test@example.com")
                .password("password")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @Test
    void createAccount_ShouldReturnAccountDTO_WhenBusinessExists() {
        // Arrange
        AccountDTO accountDTO = AccountDTO.builder()
                .businessId(1L)
                .bankAccountNumber(1111111111L)
                .ifscCode("ABC12345")
                .activationStatus(ActivationStatus.ACTIVE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        // Mock the behavior of the BusinessRepository to return the business
        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));

        // Mock the behavior of the AccountRepository to return a new Account
        Account savedAccount = Account.builder()
                .id(1L)
                .business(business) // Ensure the business is set here
                .bankAccountNumber(accountDTO.getBankAccountNumber())
                .ifscCode(accountDTO.getIfscCode())
                .activationStatus(accountDTO.getActivationStatus())
                .balance(accountDTO.getBalance())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        AccountDTO result = accountService.createAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getBusinessId());
        verify(businessRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_ShouldThrowException_WhenBusinessNotFound() {
        // Arrange
        AccountDTO accountDTO = AccountDTO.builder()
                .businessId(1L)
                .bankAccountNumber(1111111111L)
                .ifscCode("ABC12345")
                .activationStatus(ActivationStatus.ACTIVE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(accountDTO);
        });

        assertEquals("Business not Found", exception.getMessage());
    }
}
