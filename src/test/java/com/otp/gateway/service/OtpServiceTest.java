package com.otp.gateway.service;

import com.otp.gateway.model.Otp;
import com.otp.gateway.model.User;
import com.otp.gateway.repository.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateOtp_ShouldCreateAndSaveOtp() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        when(otpRepository.save(any(Otp.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        String otp = otpService.generateOtp(user);

        // Assert
        assertNotNull(otp);
        assertEquals(6, otp.length());
        verify(otpRepository).save(any(Otp.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void validateOtp_WithValidOtp_ShouldReturnTrue() {
        // Arrange
        String otpCode = "123456";
        User user = new User();
        user.setEmail("test@example.com");
        Otp otp = new Otp();
        otp.setCode(otpCode);
        otp.setUser(user);
        otp.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        when(otpRepository.findByUserAndCode(user, otpCode)).thenReturn(Optional.of(otp));

        // Act
        boolean isValid = otpService.validateOtp(user, otpCode);

        // Assert
        assertTrue(isValid);
        verify(otpRepository).delete(otp);
    }

    @Test
    void validateOtp_WithExpiredOtp_ShouldReturnFalse() {
        // Arrange
        String otpCode = "123456";
        User user = new User();
        user.setEmail("test@example.com");
        Otp otp = new Otp();
        otp.setCode(otpCode);
        otp.setUser(user);
        otp.setExpiryDate(LocalDateTime.now().minusMinutes(1));
        when(otpRepository.findByUserAndCode(user, otpCode)).thenReturn(Optional.of(otp));

        // Act
        boolean isValid = otpService.validateOtp(user, otpCode);

        // Assert
        assertFalse(isValid);
        verify(otpRepository).delete(otp);
    }
}