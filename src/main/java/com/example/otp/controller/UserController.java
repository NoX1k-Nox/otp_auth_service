package com.example.otp.controller;

import com.example.otp.service.UserService;
import com.example.otp.util.ApiResponseUtils;
import com.example.otp.util.JsonUtils;
import com.example.otp.util.SafeHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public HttpHandler register() {
        return new SafeHandler() {
            @Override
            public void handleRequest(HttpExchange exchange) throws IOException {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    ApiResponseUtils.sendBadRequest(exchange, "Only POST method allowed");
                    return;
                }

                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                RegisterRequest request = JsonUtils.fromJson(body, RegisterRequest.class);

                try {
                    userService.register(request.getLogin(), request.getPassword(), request.getRole());
                    ApiResponseUtils.sendCreated(exchange, "{\"message\":\"User registered\"}");
                } catch (IllegalArgumentException e) {
                    ApiResponseUtils.sendBadRequest(exchange, e.getMessage());
                }
            }
        };
    }

    public HttpHandler login() {
        return new SafeHandler() {
            @Override
            public void handleRequest(HttpExchange exchange) throws IOException {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    ApiResponseUtils.sendBadRequest(exchange, "Only POST method allowed");
                    return;
                }

                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                LoginRequest request = JsonUtils.fromJson(body, LoginRequest.class);

                boolean authenticated = userService.authenticate(request.getLogin(), request.getPassword());
                if (authenticated) {
                    ApiResponseUtils.sendOk(exchange, "{\"message\":\"Login successful\"}");
                } else {
                    ApiResponseUtils.sendUnauthorized(exchange, "Invalid credentials");
                }
            }
        };
    }

    private static class RegisterRequest {
        private String login;
        private String password;
        private String role;

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    private static class LoginRequest {
        private String login;
        private String password;

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
