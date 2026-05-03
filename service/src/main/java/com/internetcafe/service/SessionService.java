package com.internetcafe.service;

import com.internetcafe.domain.entities.Session;
import com.internetcafe.domain.entities.Station;
import com.internetcafe.domain.entities.Transaction;
import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.StationStatus;
import com.internetcafe.domain.enums.TransactionType;
import com.internetcafe.domain.exceptions.ActiveSessionExistsException;
import com.internetcafe.domain.exceptions.InsufficientBalanceException;
import com.internetcafe.domain.exceptions.NoActiveSessionException;
import com.internetcafe.domain.exceptions.StationNotAvailableException;
import com.internetcafe.domain.repositories.SessionRepository;
import com.internetcafe.domain.repositories.StationRepository;
import com.internetcafe.domain.repositories.TransactionRepository;
import com.internetcafe.domain.repositories.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class SessionService {
    private final SessionRepository sessionRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public SessionService(SessionRepository sessionRepository, StationRepository stationRepository,
                          UserRepository userRepository, TransactionRepository transactionRepository) {
        this.sessionRepository = sessionRepository;
        this.stationRepository = stationRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void startSession(int stationId) {
        User currentUser = SessionContext.getCurrentUser();

        if (sessionRepository.findActiveByUserId(currentUser.getId()).isPresent()) {
            throw new ActiveSessionExistsException("User already has an active session");
        }

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        if (station.getStatus() != StationStatus.AVAILABLE) {
            throw new StationNotAvailableException("Station is not available");
        }

        if (currentUser.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientBalanceException("Insufficient balance to start session");
        }

        Session session = new Session(0, currentUser.getId(), stationId, LocalDateTime.now(), null, null);
        sessionRepository.save(session);

        station.setStatus(StationStatus.IN_USE);
        stationRepository.update(station);
    }

    public void endSession() {
        User currentUser = SessionContext.getCurrentUser();

        Session activeSession = sessionRepository.findActiveByUserId(currentUser.getId())
                .orElseThrow(() -> new NoActiveSessionException("No active session found"));

        Station station = stationRepository.findById(activeSession.getStationId())
                .orElseThrow(() -> new RuntimeException("Station not found"));

        LocalDateTime endTime = LocalDateTime.now();
        long minutes = Duration.between(activeSession.getStartTime(), endTime).toMinutes();

        BigDecimal durationHours = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal fullCost = durationHours.multiply(station.getHourlyRate())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal chargedAmount = currentUser.getBalance().min(fullCost);
        BigDecimal newBalance = currentUser.getBalance().subtract(chargedAmount);

        activeSession.setEndTime(endTime);
        activeSession.setCost(chargedAmount);
        sessionRepository.update(activeSession);

        currentUser.setBalance(newBalance);
        userRepository.update(currentUser);

        Transaction transaction = new Transaction(
                0,
                currentUser.getId(),
                chargedAmount,
                TransactionType.DEDUCTION,
                LocalDateTime.now()
        );
        transactionRepository.save(transaction);

        station.setStatus(StationStatus.AVAILABLE);
        stationRepository.update(station);

        SessionContext.login(currentUser);
    }

    public Session getActiveSession() {
        User currentUser = SessionContext.getCurrentUser();
        return sessionRepository.findActiveByUserId(currentUser.getId())
                .orElseThrow(() -> new NoActiveSessionException("No active session found"));
    }
}
