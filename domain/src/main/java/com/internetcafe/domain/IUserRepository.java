package com.internetcafe.domain;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByUsername(String username);

    void save(User user);

    void update(User user);

    List<User> findAll();
}
