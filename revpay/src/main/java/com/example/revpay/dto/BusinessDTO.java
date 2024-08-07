package com.example.revpay.dto;

import com.example.revpay.model.Business;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String jwt;

    public static BusinessDTO fromEntity(Business business) {
        return BusinessDTO.builder()
                .id(business.getId())
                .name(business.getName())
                .email(business.getEmail())
                .build();
    }

    public Business toEntity() {
        return Business.builder()
                .name(name)
                .email(email)
                .password(password)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
