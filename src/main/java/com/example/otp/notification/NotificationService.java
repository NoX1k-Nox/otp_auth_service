package com.example.otp.notification;

import com.example.otp.model.OtpCode;

import java.util.List;

public class NotificationService {
    private final List<NotificationChannel> channels;

    public NotificationService(List<NotificationChannel> channels) {
        this.channels = channels;
    }

    public void send(OtpCode otpCode) {
        for (NotificationChannel channel : channels) {
            channel.send(otpCode);
        }
    }
}
