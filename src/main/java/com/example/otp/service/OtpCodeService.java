package com.example.otp.service;

public interface OtpCodeService {
    String generateOtp(String body);
    boolean validateOtp(String body);

    void expireOldCodes();
}