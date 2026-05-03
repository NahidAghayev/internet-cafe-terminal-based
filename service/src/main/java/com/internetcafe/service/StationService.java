package com.internetcafe.service;

import com.internetcafe.domain.entities.Station;
import com.internetcafe.domain.enums.StationStatus;
import com.internetcafe.domain.repositories.StationRepository;
import java.math.BigDecimal;
import java.util.List;

public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public List<Station> getAvailableStations() {
        return stationRepository.findAllAvailable();
    }

    public void addStation(String name, String specs, BigDecimal hourlyRate) {
        Station station = new Station(0, name, specs, StationStatus.AVAILABLE, hourlyRate);
        stationRepository.save(station);
    }

    public void updateStation(int id, String name, String specs, BigDecimal hourlyRate, StationStatus status) {
        Station station = new Station(id, name, specs, status, hourlyRate);
        stationRepository.update(station);
    }

    public void deleteStation(int id) {
        stationRepository.delete(id);
    }
}
