package com.example.otp.dto;

import java.util.UUID;

public class GenerateOtpRequest {
    private UUID userId;
    private String operationId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
