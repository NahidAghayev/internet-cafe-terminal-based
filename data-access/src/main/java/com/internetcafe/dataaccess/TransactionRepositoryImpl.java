package com.internetcafe.dataaccess;

import com.internetcafe.domain.entities.Transaction;
import com.internetcafe.domain.enums.TransactionType;
import com.internetcafe.domain.repositories.TransactionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Connection connection;

    public TransactionRepositoryImpl(ConnectionManager connectionManager) {
        this.connection = connectionManager.getConnection();
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, amount, type, created_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getUserId());
            statement.setBigDecimal(2, transaction.getAmount());
            statement.setString(3, transaction.getType().name());
            statement.setTimestamp(4, Timestamp.valueOf(transaction.getCreatedAt()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    @Override
    public List<Transaction> findByUserId(int userId) {
        String sql = "SELECT id, user_id, amount, type, created_at FROM transactions WHERE user_id = ? ORDER BY created_at DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapTransaction(resultSet));
                }
            }

            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions by user id", e);
        }
    }

    private Transaction mapTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getBigDecimal("amount"),
                TransactionType.valueOf(resultSet.getString("type")),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
