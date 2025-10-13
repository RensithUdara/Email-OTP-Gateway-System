package com.otp.gateway.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom("noreply@otpgateway.com");
        helper.setTo(to);
        helper.setSubject("Your OTP Code");
        
        String htmlContent = String.format("""
                <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: auto;">
                    <h2 style="color: #333;">OTP Verification</h2>
                    <p>Your OTP code is:</p>
                    <h1 style="color: #007bff; font-size: 32px; letter-spacing: 5px; margin: 20px 0;">%s</h1>
                    <p>This code will expire in 5 minutes.</p>
                    <p style="color: #666; font-size: 12px;">If you didn't request this code, please ignore this email.</p>
                </div>
                """, otp);
        
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}