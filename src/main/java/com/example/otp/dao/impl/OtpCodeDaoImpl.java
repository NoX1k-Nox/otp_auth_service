package com.example.otp.dao.impl;

import com.example.otp.dao.OtpCodeDao;
import com.example.otp.model.OtpCode;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class OtpCodeDaoImpl implements OtpCodeDao {
    private final Connection connection;

    public OtpCodeDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(OtpCode code) {
        String sql = "INSERT INTO otp_codes (user_id, code, operation_id, status, created_at, expires_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, code.getUserId());
            ps.setString(2, code.getCode());
            ps.setString(3, code.getOperationId());
            ps.setString(4, code.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(code.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(code.getExpiresAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving OTP code", e);
        }
    }

    @Override
    public Optional<OtpCode> findActiveByUserIdAndCode(int userId, String code) {
        String sql = "SELECT * FROM otp_codes WHERE user_id = ? AND code = ? AND status = 'ACTIVE'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding active OTP code", e);
        }
        return Optional.empty();
    }

    @Override
    public void updateStatus(int codeId, String status) {
        String sql = "UPDATE otp_codes SET status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, codeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating OTP status", e);
        }
    }

    @Override
    public void expireOldCodes() {
        String sql = "UPDATE otp_codes SET status = 'EXPIRED' WHERE expires_at < ? AND status = 'ACTIVE'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error expiring OTP codes", e);
        }
    }

    @Override
    public void deleteAllByUserId(int userId) {
        String sql = "DELETE FROM otp_codes WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting OTP codes by user ID", e);
        }
    }

    @Override
    public Optional<OtpCode> findByCode(String code) {
        String sql = "SELECT * FROM otp_codes WHERE code = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding OTP by code", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(OtpCode code) {
        String sql = "UPDATE otp_codes SET user_id = ?, code = ?, operation_id = ?, status = ?, created_at = ?, expires_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, code.getUserId());
            ps.setString(2, code.getCode());
            ps.setString(3, code.getOperationId());
            ps.setString(4, code.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(code.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(code.getExpiresAt()));
            ps.setInt(7, code.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating OTP code", e);
        }
    }

    private OtpCode mapRow(ResultSet rs) throws SQLException {
        return new OtpCode(
                rs.getInt("id"),
                rs.getObject("user_id", UUID.class),
                rs.getString("code"),
                rs.getString("operation_id"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("expires_at").toLocalDateTime()
        );
    }
}
