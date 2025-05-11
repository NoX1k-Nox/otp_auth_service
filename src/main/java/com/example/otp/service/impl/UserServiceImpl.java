package com.example.otp.service.impl;

import com.example.otp.dao.UserDao;
import com.example.otp.model.User;
import com.example.otp.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userDao.findByUsername(login);
    }

    @Override
    public void register(String login, String password, String role) {
        Optional<User> existingUser = userDao.findByUsername(login);
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        String hashedPassword = hashPassword(password);
        User user = new User(0, login, hashedPassword, role);
        userDao.save(user);
    }

    @Override
    public boolean authenticate(String login, String password) {
        Optional<User> user = userDao.findByUsername(login);
        return user.map(u -> u.getPasswordHash().equals(password)).orElse(false);
    }

    @Override
    public String login(String login, String password) {
        if (authenticate(login, password)) {
            return "{\"message\":\"Login successful\", \"username\":\"" + login + "\"}";
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}
