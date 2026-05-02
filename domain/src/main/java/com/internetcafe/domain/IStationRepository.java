package com.internetcafe.domain;

import java.util.List;
import java.util.Optional;

public interface IStationRepository {
    Optional<Station> findById(int id);

    List<Station> findAll();

    List<Station> findAllAvailable();

    void save(Station station);

    void update(Station station);

    void delete(int id);
}
