package com.internetcafe.domain.entities;

import com.internetcafe.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int userId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime createdAt;

    public Transaction(int id, int userId, BigDecimal amount, TransactionType type, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
