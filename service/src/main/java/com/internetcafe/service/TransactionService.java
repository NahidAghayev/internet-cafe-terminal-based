package com.internetcafe.service;

import com.internetcafe.domain.entities.Transaction;
import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.TransactionType;
import com.internetcafe.domain.repositories.TransactionRepository;
import com.internetcafe.domain.repositories.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void topUp(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Top-up amount must be greater than zero");
        }

        User currentUser = SessionContext.getCurrentUser();
        BigDecimal newBalance = currentUser.getBalance().add(amount);
        currentUser.setBalance(newBalance);
        userRepository.update(currentUser);

        Transaction transaction = new Transaction(
                0,
                currentUser.getId(),
                amount,
                TransactionType.TOP_UP,
                LocalDateTime.now()
        );
        transactionRepository.save(transaction);

        SessionContext.login(currentUser);
    }

    public List<Transaction> getHistory() {
        User currentUser = SessionContext.getCurrentUser();
        return transactionRepository.findByUserId(currentUser.getId());
    }
}
