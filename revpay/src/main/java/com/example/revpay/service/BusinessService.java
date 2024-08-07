package com.example.revpay.service;

import com.example.revpay.dto.BusinessDTO;

public interface BusinessService {
    public BusinessDTO register(BusinessDTO request);
    public BusinessDTO login(BusinessDTO request);
    public BusinessDTO resetPassword(BusinessDTO request);
    public BusinessDTO resetUsername(BusinessDTO request);
    public BusinessDTO findById(Long businessId);
    public BusinessDTO findByEmail(String email);
}
