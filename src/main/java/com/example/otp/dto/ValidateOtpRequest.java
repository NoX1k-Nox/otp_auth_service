package com.example.otp.dto;

public class ValidateOtpRequest {
    private final String otpCode;

    public ValidateOtpRequest(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getOtpCode() {
        return otpCode;
    }
}

