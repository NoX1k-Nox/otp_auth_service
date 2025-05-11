package com.example.otp.notification.impl;

import com.example.otp.model.OtpCode;
import com.example.otp.notification.NotificationChannel;

public class TelegramNotificationService implements NotificationChannel {

    @Override
    public void send(OtpCode otpCode) {
        System.out.printf("[Telegram] Sent OTP '%s' to user %s for operation %s%n",
                otpCode.getCode(),
                otpCode.getUserId(),
                otpCode.getOperationId());
    }
}
