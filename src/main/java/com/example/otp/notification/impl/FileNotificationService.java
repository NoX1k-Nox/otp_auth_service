package com.example.otp.notification.impl;

import com.example.otp.model.OtpCode;
import com.example.otp.notification.NotificationChannel;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FileNotificationService implements NotificationChannel {
    private static final String FILE_PATH = "otp_codes.log";

    @Override
    public void send(OtpCode otpCode) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(String.format("Code: %s | User: %s | Operation: %s | Expires: %s%n",
                    otpCode.getCode(),
                    otpCode.getUserId(),
                    otpCode.getOperationId(),
                    otpCode.getExpiresAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ));
        } catch (IOException e) {
            System.err.println("[File] Error writing OTP to file: " + e.getMessage());
        }
    }
}
