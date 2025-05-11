package com.example.otp.notification;

import com.example.otp.model.OtpCode;

public interface NotificationChannel {
    void send(OtpCode otpCode);
}
