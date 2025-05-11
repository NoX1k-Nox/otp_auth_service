package com.example.otp.dao.impl;

import com.example.otp.dao.OtpConfigurationDao;
import com.example.otp.model.OtpConfiguration;
import java.util.Optional;

import java.sql.*;

public class OtpConfigurationDaoImpl implements OtpConfigurationDao {
    private final Connection connection;

    public OtpConfigurationDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveOrUpdate(OtpConfiguration config) {
        try {
            connection.setAutoCommit(false);
            Statement clear = connection.createStatement();
            clear.executeUpdate("DELETE FROM otp_config");

            String sql = "INSERT INTO otp_config (length, ttl_seconds) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, config.getCodeLength());
                ps.setInt(2, config.getTtlSeconds());
                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Rollback failed", rollbackEx);
            }
            throw new RuntimeException("Failed to save OTP configuration", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {}
        }
    }

    @Override
    public Optional<OtpConfiguration> get() {
        String sql = "SELECT * FROM otp_config LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return Optional.of(new OtpConfiguration(
                        rs.getInt("length"),
                        rs.getInt("ttl_seconds")
                ));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading OTP configuration", e);
        }
    }
}
