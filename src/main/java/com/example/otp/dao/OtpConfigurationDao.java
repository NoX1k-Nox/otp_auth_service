package com.example.otp.dao;

import com.example.otp.model.OtpConfiguration;

import java.util.Optional;

public interface OtpConfigurationDao {
    void saveOrUpdate(OtpConfiguration config);
    Optional<OtpConfiguration> get();
}
