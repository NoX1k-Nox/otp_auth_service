package com.example.otp;

import com.example.otp.controller.OtpController;
import com.example.otp.controller.UserController;
import com.sun.net.httpserver.HttpServer;

public class Router {

    private final HttpServer server;
    private final UserController userController;
    private final OtpController otpController;

    public Router(HttpServer server, UserController userController, OtpController otpController) {
        this.server = server;
        this.userController = userController;
        this.otpController = otpController;
    }

    public void registerRoutes() {
        server.createContext("/api/register", userController.register());
        server.createContext("/api/login", userController.login());

        server.createContext("/otp/generate", otpController.generateOtp());
        server.createContext("/otp/validate", otpController.validateOtp());
    }
}
