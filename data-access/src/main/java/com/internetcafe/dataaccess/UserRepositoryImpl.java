package com.internetcafe.dataaccess;

import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.UserRole;
import com.internetcafe.domain.repositories.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryImpl(ConnectionManager connectionManager) {
        this.connection = connectionManager.getConnection();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, role, balance, created_at FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by username", e);
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password_hash, role, balance, created_at) VALUES (?, ?, ?::user_role, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, hash(user.getPasswordHash()));
            statement.setString(3, user.getRole().name());
            statement.setBigDecimal(4, user.getBalance());
            statement.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET balance = ?, role = ?::user_role WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, user.getBalance());
            statement.setString(2, user.getRole().name());
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, username, password_hash, role, balance, created_at FROM users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                UserRole.valueOf(resultSet.getString("role")),
                resultSet.getBigDecimal("balance"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    private String hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();

            for (byte hashByte : hashBytes) {
                stringBuilder.append(String.format("%02x", hashByte));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm is not available", e);
        }
    }
}
