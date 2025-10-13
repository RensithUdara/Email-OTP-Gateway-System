package com.otp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailOtpGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailOtpGatewayApplication.class, args);
    }
}