package com.otp.gateway.service;

import com.otp.gateway.model.Otp;
import com.otp.gateway.model.User;
import com.otp.gateway.repository.OtpRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    
    @Value("${application.otp.length:6}")
    private int otpLength;
    
    @Value("${application.otp.rate-limit:3}")
    private int maxAttempts;

    private final SecureRandom random = new SecureRandom();

    public void generateAndSendOtp(User user) throws MessagingException {
        // Check for existing OTP
        otpRepository.findTopByUserOrderByExpiryTimeDesc(user)
                .ifPresent(existingOtp -> {
                    if (existingOtp.getExpiryTime().isAfter(LocalDateTime.now()) && 
                        existingOtp.getAttempts() >= maxAttempts) {
                        throw new RuntimeException("Too many OTP attempts. Please try again later.");
                    }
                });

        // Generate new OTP
        String otpCode = generateOtp();
        
        // Save OTP
        Otp otp = Otp.builder()
                .user(user)
                .code(otpCode)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .attempts(0)
                .used(false)
                .build();
        
        otpRepository.save(otp);
        
        // Send OTP via email
        emailService.sendOtpEmail(user.getEmail(), otpCode);
    }

    public boolean validateOtp(User user, String code) {
        Otp otp = otpRepository.findByUserAndCodeAndUsedFalseAndExpiryTimeAfter(
                user, code, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        if (otp.getAttempts() >= maxAttempts) {
            throw new RuntimeException("Too many attempts. OTP is now invalid.");
        }

        otp.setAttempts(otp.getAttempts() + 1);
        
        if (!otp.getCode().equals(code)) {
            otpRepository.save(otp);
            return false;
        }

        otp.setUsed(true);
        otpRepository.save(otp);
        return true;
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}