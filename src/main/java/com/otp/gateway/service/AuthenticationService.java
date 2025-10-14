package com.otp.gateway.service;

import com.otp.gateway.model.Role;
import com.otp.gateway.model.User;
import com.otp.gateway.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    public String register(String email, String password, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        var user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .enabled(false)
                .build();

        userRepository.save(user);
        
        try {
            otpService.generateAndSendOtp(user);
            return "Registration successful. Please verify your email with the OTP sent.";
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification OTP");
        }
    }

    public String authenticate(String email, String password) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified");
        }

        return jwtService.generateToken(user);
    }

    public void verifyEmail(String email, String otp) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (otpService.validateOtp(user, otp)) {
            user.setEnabled(true);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    public void initiatePasswordReset(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        otpService.generateAndSendOtp(user);
    }

    public void resetPassword(String email, String otp, String newPassword) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (otpService.validateOtp(user, otp)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }
}