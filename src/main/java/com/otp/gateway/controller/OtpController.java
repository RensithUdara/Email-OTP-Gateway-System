package com.otp.gateway.controller;

import com.otp.gateway.model.User;
import com.otp.gateway.repository.UserRepository;
import com.otp.gateway.service.OtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;
    private final UserRepository userRepository;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@AuthenticationPrincipal User user) throws MessagingException {
        otpService.generateAndSendOtp(user);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(
            @AuthenticationPrincipal User user,
            @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(user, otp);
        return ResponseEntity.ok(isValid ? "OTP verified successfully" : "Invalid OTP");
    }
}