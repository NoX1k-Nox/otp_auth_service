package com.example.otp.service.impl;

import com.example.otp.dao.OtpConfigurationDao;
import com.example.otp.model.OtpConfiguration;
import com.example.otp.service.OtpConfigurationService;

import java.util.Optional;

public class OtpConfigurationServiceImpl implements OtpConfigurationService {
    private final OtpConfigurationDao otpConfigurationDao;

    public OtpConfigurationServiceImpl(OtpConfigurationDao otpConfigurationDao) {
        this.otpConfigurationDao = otpConfigurationDao;
    }

    @Override
    public Optional<OtpConfiguration> getConfiguration() {
        return otpConfigurationDao.get();
    }

    @Override
    public void updateConfiguration(OtpConfiguration config) {
        if (config.getCodeLength() < 4 || config.getCodeLength() > 10) {
            throw new IllegalArgumentException("OTP length must be between 4 and 10 digits.");
        }

        if (config.getTtlSeconds() < 10 || config.getTtlSeconds() > 600) {
            throw new IllegalArgumentException("OTP TTL must be between 10 and 600 seconds.");
        }

        otpConfigurationDao.saveOrUpdate(config);
    }
}
