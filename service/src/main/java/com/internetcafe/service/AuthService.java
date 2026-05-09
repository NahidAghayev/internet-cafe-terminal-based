package com.internetcafe.service;

import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.UserRole;
import com.internetcafe.domain.exceptions.AuthenticationException;
import com.internetcafe.domain.exceptions.UserAlreadyExistsException;
import com.internetcafe.domain.repositories.UserRepository;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!user.getPasswordHash().equals(hash(password))) {
            throw new AuthenticationException("Invalid username or password");
        }

        SessionContext.login(user);
        return user;
    }

    public void logout() {
        SessionContext.logout();
    }

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User(
                0,
                username,
                password,
                UserRole.CUSTOMER,
                BigDecimal.ZERO,
                LocalDateTime.now()
        );

        userRepository.save(user);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
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
