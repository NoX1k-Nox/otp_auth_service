package com.example.otp.dao;

import com.example.otp.model.OtpCode;
import java.util.Optional;

public interface OtpCodeDao {
    void save(OtpCode otp);
    Optional<OtpCode> findActiveByUserIdAndCode(int userId, String code);
    Optional<OtpCode> findByCode(String code);
    void update(OtpCode otp);
    void updateStatus(int otpId, String status);
    void expireOldCodes();
    void deleteAllByUserId(int userId);
}
