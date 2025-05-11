package com.example.otp.dao;

import com.example.otp.model.User;
import java.util.Optional;
import java.util.List;

public interface UserDao {
    void save(User user);
    Optional<User> findByUsername(String username);
    List<User> findAllExceptAdmins();
    boolean isAdminExists();
    void deleteUser(int userId);
}
