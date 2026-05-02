package com.internetcafe.dataaccess;

import com.internetcafe.domain.entities.Session;
import com.internetcafe.domain.repositories.SessionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRepositoryImpl implements SessionRepository {
    private final Connection connection;

    public SessionRepositoryImpl(ConnectionManager connectionManager) {
        this.connection = connectionManager.getConnection();
    }

    @Override
    public void save(Session session) {
        String sql = "INSERT INTO sessions (user_id, station_id, start_time, end_time, cost) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, session.getUserId());
            statement.setInt(2, session.getStationId());
            statement.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            statement.setTimestamp(4, session.getEndTime() == null ? null : Timestamp.valueOf(session.getEndTime()));
            statement.setBigDecimal(5, session.getCost());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session", e);
        }
    }

    @Override
    public void update(Session session) {
        String sql = "UPDATE sessions SET end_time = ?, cost = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, session.getEndTime() == null ? null : Timestamp.valueOf(session.getEndTime()));
            statement.setBigDecimal(2, session.getCost());
            statement.setInt(3, session.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update session", e);
        }
    }

    @Override
    public Optional<Session> findActiveByUserId(int userId) {
        String sql = "SELECT id, user_id, station_id, start_time, end_time, cost FROM sessions WHERE user_id = ? AND end_time IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapSession(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find active session by user id", e);
        }
    }

    @Override
    public List<Session> findAll() {
        String sql = "SELECT id, user_id, station_id, start_time, end_time, cost FROM sessions";
        List<Session> sessions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                sessions.add(mapSession(resultSet));
            }
            return sessions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all sessions", e);
        }
    }

    @Override
    public List<Session> findByUserId(int userId) {
        String sql = "SELECT id, user_id, station_id, start_time, end_time, cost FROM sessions WHERE user_id = ?";
        List<Session> sessions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(mapSession(resultSet));
                }
            }

            return sessions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find sessions by user id", e);
        }
    }

    private Session mapSession(ResultSet resultSet) throws SQLException {
        Timestamp endTime = resultSet.getTimestamp("end_time");

        return new Session(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("station_id"),
                resultSet.getTimestamp("start_time").toLocalDateTime(),
                endTime == null ? null : endTime.toLocalDateTime(),
                resultSet.getBigDecimal("cost")
        );
    }
}
