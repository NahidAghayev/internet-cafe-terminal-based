package com.internetcafe.domain.repositories;

import com.internetcafe.domain.entities.Session;
import java.util.List;
import java.util.Optional;

public interface ISessionRepository {
    void save(Session session);

    void update(Session session);

    Optional<Session> findActiveByUserId(int userId);

    List<Session> findAll();

    List<Session> findByUserId(int userId);
}
