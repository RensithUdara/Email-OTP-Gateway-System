package com.otp.gateway.controller;

import com.otp.gateway.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {
        return ResponseEntity.ok(authService.register(email, password, firstName, lastName));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        authService.verifyEmail(email, otp);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @RequestParam String email,
            @RequestParam String password
    ) {
        return ResponseEntity.ok(authService.authenticate(email, password));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws MessagingException {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset OTP sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {
        authService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }
}