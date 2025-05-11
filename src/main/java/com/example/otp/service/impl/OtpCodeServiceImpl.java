package com.example.otp.service.impl;

import com.example.otp.dao.OtpCodeDao;
import com.example.otp.dto.GenerateOtpRequest;
import com.example.otp.dto.ValidateOtpRequest;
import com.example.otp.model.OtpCode;
import com.example.otp.model.enums.OtpStatus;
import com.example.otp.service.OtpCodeService;
import com.example.otp.service.OtpConfigurationService;
import com.example.otp.util.JsonUtils;
import com.example.otp.notification.NotificationService;
import com.example.otp.model.OtpConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

public class OtpCodeServiceImpl implements OtpCodeService {
    private final OtpCodeDao otpCodeDao;
    private final OtpConfigurationService otpConfigurationService;
    private final NotificationService notificationService;

    public OtpCodeServiceImpl(OtpCodeDao otpCodeDao,
                              OtpConfigurationService otpConfigurationService,
                              NotificationService notificationService) {
        this.otpCodeDao = otpCodeDao;
        this.otpConfigurationService = otpConfigurationService;
        this.notificationService = notificationService;
    }

    @Override
    public String generateOtp(String body) {
        GenerateOtpRequest request = JsonUtils.fromJson(body, GenerateOtpRequest.class);

        OtpConfiguration config = otpConfigurationService.getConfiguration()
                .orElseThrow(() -> new IllegalStateException("OTP configuration not found"));

        int codeLength = config.getCodeLength();
        String generatedCode = generateRandomCode(codeLength);

        int ttlSeconds = config.getTtlSeconds();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(ttlSeconds);

        OtpCode otpCode = new OtpCode();
        otpCode.setUserId(request.getUserId());
        otpCode.setOperationId(request.getOperationId());
        otpCode.setCode(generatedCode);
        otpCode.setStatus(OtpStatus.ACTIVE.name());
        otpCode.setExpiresAt(expiresAt);
        otpCode.setCreatedAt(LocalDateTime.now()); // Не забудь!

        otpCodeDao.save(otpCode);
        notificationService.send(otpCode);

        return "{\"code\":\"" + generatedCode + "\"}";
    }

    @Override
    public boolean validateOtp(String body) {
        ValidateOtpRequest request = JsonUtils.fromJson(body, ValidateOtpRequest.class);

        Optional<OtpCode> otpCode = otpCodeDao.findByCode(request.getOtpCode());
        if (otpCode.isEmpty()) {
            return false;
        }

        if (!otpCode.get().getStatus().equals(OtpStatus.ACTIVE.name())) {
            return false;
        }

        if (otpCode.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            otpCode.get().setStatus(OtpStatus.EXPIRED.name());
            otpCodeDao.update(otpCode.orElse(null));
            return false;
        }

        otpCode.get().setStatus(OtpStatus.USED.name());
        otpCodeDao.update(otpCode.orElse(null));
        return true;
    }

    private String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }

    @Override
    public void expireOldCodes() {
        System.out.println("[Scheduler] Running OTP expiration task...");
        otpCodeDao.expireOldCodes();
        System.out.println("[Scheduler] OTP expiration task completed.");
    }
}
