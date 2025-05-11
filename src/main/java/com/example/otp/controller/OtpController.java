package com.example.otp.controller;

import com.example.otp.service.OtpCodeService;
import com.example.otp.util.ApiResponseUtils;
import com.example.otp.util.SafeHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OtpController {
    private final OtpCodeService otpCodeService;

    public OtpController(OtpCodeService otpCodeService) {
        this.otpCodeService = otpCodeService;
    }

    public HttpHandler generateOtp() {
        return new SafeHandler() {
            @Override
            public void handleRequest(HttpExchange exchange) throws IOException {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    ApiResponseUtils.sendBadRequest(exchange, "Only POST method allowed");
                    return;
                }

                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                try {
                    String result = otpCodeService.generateOtp(body);
                    ApiResponseUtils.sendCreated(exchange, result);
                } catch (Exception e) {
                    ApiResponseUtils.sendInternalServerError(exchange, e.getMessage());
                }
            }
        };
    }

    public HttpHandler validateOtp() {
        return new SafeHandler() {
            @Override
            public void handleRequest(HttpExchange exchange) throws IOException {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    ApiResponseUtils.sendBadRequest(exchange, "Only POST method allowed");
                    return;
                }

                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                try {
                    boolean isValid = otpCodeService.validateOtp(body);
                    if (isValid) {
                        ApiResponseUtils.sendOk(exchange, "{\"message\":\"OTP code is valid\"}");
                    } else {
                        ApiResponseUtils.sendBadRequest(exchange, "Invalid or expired OTP code");
                    }
                } catch (Exception e) {
                    ApiResponseUtils.sendInternalServerError(exchange, e.getMessage());
                }
            }
        };
    }
}
