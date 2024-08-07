package com.example.revpay.controller;

import com.example.revpay.dto.AuthRequest;
import com.example.revpay.dto.BusinessDTO;
import com.example.revpay.service.BusinessService;
import com.example.revpay.security.CustomUserDetailsService;
import com.example.revpay.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusinessDTO> register(@RequestBody BusinessDTO request) {
        BusinessDTO response = businessService.register(request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/login")
//    public ResponseEntity<BusinessDTO> login(@RequestBody BusinessDTO request) {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getEmail());
        Long businessId = businessService.findByEmail(authRequest.getEmail()).getId();
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), businessId);

        // Load the business entity from the database
        BusinessDTO businessDTO = businessService.findByEmail(authRequest.getEmail());
        businessDTO.setJwt(jwt);

        return ResponseEntity.ok(businessDTO);

//        BusinessDTO response = businessService.login(request);
//        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Long businessId) {
        businessService.logOut(businessId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<BusinessDTO> resetPassword(@RequestBody BusinessDTO request) {
        BusinessDTO response = businessService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reset-username")
    public ResponseEntity<BusinessDTO> resetUsername(@RequestBody BusinessDTO request) {
        BusinessDTO response = businessService.resetUsername(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessDTO> getBusinessById(@PathVariable Long businessId) {
        BusinessDTO response = businessService.findById(businessId);
        return ResponseEntity.ok(response);
    }
}
