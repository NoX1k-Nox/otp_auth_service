package com.example.otp.service;

import com.example.otp.model.OtpConfiguration;

import java.util.Optional;

public interface OtpConfigurationService {
    Optional<OtpConfiguration> getConfiguration();
    void updateConfiguration(OtpConfiguration config);
}
