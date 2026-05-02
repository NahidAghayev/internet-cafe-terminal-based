package com.internetcafe.domain.repositories;

import com.internetcafe.domain.entities.Transaction;
import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);

    List<Transaction> findByUserId(int userId);
}
