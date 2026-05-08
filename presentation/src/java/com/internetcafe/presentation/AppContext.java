package com.internetcafe.presentation;

import com.internetcafe.dataaccess.*;
import com.internetcafe.domain.repositories.*;
import com.internetcafe.service.*;


public class AppContext {
    private final AuthService authService;
    private final StationService stationService;
    private final TransactionService transactionService;
    private final SessionService sessionService;

    public AppContext(ConnectionManager connectionManager) {
        UserRepository userRepository = new UserRepositoryImpl(connectionManager);
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(connectionManager);
        SessionRepository sessionRepository = new SessionRepositoryImpl(connectionManager);
        StationRepository stationRepository = new StationRepositoryImpl(connectionManager);

        this.authService = new AuthService(userRepository);
        this.transactionService = new TransactionService(transactionRepository, userRepository);
        this.stationService = new StationService(stationRepository);
        this.sessionService = new SessionService(sessionRepository, stationRepository, userRepository, transactionRepository);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public StationService getStationService() {
        return stationService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }
}