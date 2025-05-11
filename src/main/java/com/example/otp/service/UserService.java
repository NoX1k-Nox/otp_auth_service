package com.example.otp.service;

import com.example.otp.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
    void register(String login, String password, String role);
    boolean authenticate(String login, String password);

    String login(String login, String password);
}
