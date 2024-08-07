package com.example.revpay;

import com.example.revpay.dto.BusinessDTO;
import com.example.revpay.model.Business;
import com.example.revpay.repository.BusinessRepository;
import com.example.revpay.service.BusinessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessServiceTest {

    @InjectMocks
    private BusinessServiceImpl businessService;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    void register_ShouldReturnBusinessDTO_WhenEmailIsUnique() {
        // Arrange
        BusinessDTO businessDTO = BusinessDTO.builder()
                .name("Test Business")
                .email("test@example.com")
                .password("password")
                .build();

        when(businessRepository.findByEmail(businessDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(businessDTO.getPassword())).thenReturn("encodedPassword");
        when(businessRepository.save(any(Business.class))).thenReturn(business);

        // Act
        BusinessDTO result = businessService.register(businessDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Business", result.getName());
        verify(businessRepository).findByEmail(businessDTO.getEmail());
        verify(passwordEncoder).encode(businessDTO.getPassword());
        verify(businessRepository).save(any(Business.class));
    }


    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        BusinessDTO businessDTO = BusinessDTO.builder()
                .name("Test Business")
                .email("test@example.com")
                .password("password")
                .build();

        when(businessRepository.findByEmail(businessDTO.getEmail())).thenReturn(Optional.of(business));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            businessService.register(businessDTO);
        });

        assertEquals("Email already in use", exception.getMessage());
    }
}

