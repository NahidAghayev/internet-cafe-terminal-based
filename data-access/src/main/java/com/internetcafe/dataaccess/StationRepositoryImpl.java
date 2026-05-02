package com.internetcafe.dataaccess;

import com.internetcafe.domain.entities.Station;
import com.internetcafe.domain.enums.StationStatus;
import com.internetcafe.domain.repositories.StationRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StationRepositoryImpl implements StationRepository {
    private final Connection connection;

    public StationRepositoryImpl(ConnectionManager connectionManager) {
        this.connection = connectionManager.getConnection();
    }

    @Override
    public Optional<Station> findById(int id) {
        String sql = "SELECT id, name, specs, status, hourly_rate FROM stations WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapStation(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find station by id", e);
        }
    }

    @Override
    public List<Station> findAll() {
        String sql = "SELECT id, name, specs, status, hourly_rate FROM stations";
        List<Station> stations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                stations.add(mapStation(resultSet));
            }
            return stations;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all stations", e);
        }
    }

    @Override
    public List<Station> findAllAvailable() {
        String sql = "SELECT id, name, specs, status, hourly_rate FROM stations WHERE status = 'AVAILABLE'";
        List<Station> stations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                stations.add(mapStation(resultSet));
            }
            return stations;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find available stations", e);
        }
    }

    @Override
    public void save(Station station) {
        String sql = "INSERT INTO stations (name, specs, status, hourly_rate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, station.getName());
            statement.setString(2, station.getSpecs());
            statement.setString(3, station.getStatus().name());
            statement.setBigDecimal(4, station.getHourlyRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save station", e);
        }
    }

    @Override
    public void update(Station station) {
        String sql = "UPDATE stations SET name = ?, specs = ?, status = ?, hourly_rate = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, station.getName());
            statement.setString(2, station.getSpecs());
            statement.setString(3, station.getStatus().name());
            statement.setBigDecimal(4, station.getHourlyRate());
            statement.setInt(5, station.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update station", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM stations WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete station", e);
        }
    }

    private Station mapStation(ResultSet resultSet) throws SQLException {
        return new Station(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("specs"),
                StationStatus.valueOf(resultSet.getString("status")),
                resultSet.getBigDecimal("hourly_rate")
        );
    }
}
