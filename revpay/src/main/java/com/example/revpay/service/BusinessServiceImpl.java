package com.example.revpay.service;

import com.example.revpay.dto.*;
import com.example.revpay.model.Business;
import com.example.revpay.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService{

    private final BusinessRepository businessRepository;

    private final PasswordEncoder passwordEncoder;

    public BusinessDTO register(BusinessDTO request){
        if(businessRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already in use");
        }

        Business business = request.toEntity();
        business.setPassword(passwordEncoder.encode(request.getPassword()));
        business = businessRepository.save(business);

        return BusinessDTO.fromEntity(business);
    }

    public BusinessDTO login(BusinessDTO request){
        Business business = businessRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email or Password"));

        if(!passwordEncoder.matches(request.getPassword(), business.getPassword())){
            throw new IllegalArgumentException("Invalid Email or Password");
        }
        return BusinessDTO.fromEntity(business);
    }

    public void logOut(Long businessId){

    }

    public BusinessDTO resetPassword(BusinessDTO request){
        Business business = businessRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

        business.setPassword(passwordEncoder.encode(request.getPassword()));
        business.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        business = businessRepository.save(business);

        return BusinessDTO.fromEntity(business);
    }

    public BusinessDTO resetUsername(BusinessDTO request){
        Business business = businessRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Business Not Found"));

        business.setEmail(request.getEmail());
        business.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        business = businessRepository.save(business);

        return BusinessDTO.fromEntity(business);
    }

    public BusinessDTO findById(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

        return BusinessDTO.fromEntity(business);
    }

    public BusinessDTO findByEmail(String email) {
        Business business = businessRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Business not found with email: " + email));
        return BusinessDTO.fromEntity(business);
    }
}
