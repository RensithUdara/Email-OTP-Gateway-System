package com.otp.gateway.repository;

import com.otp.gateway.model.Otp;
import com.otp.gateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserAndCodeAndUsedFalseAndExpiryTimeAfter(
            User user, 
            String code, 
            LocalDateTime now
    );
    
    Optional<Otp> findTopByUserOrderByExpiryTimeDesc(User user);
}