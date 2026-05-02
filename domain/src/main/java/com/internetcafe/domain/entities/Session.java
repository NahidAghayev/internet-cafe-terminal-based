package com.internetcafe.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Session {
    private int id;
    private int userId;
    private int stationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal cost;

    public Session(int id, int userId, int stationId, LocalDateTime startTime, LocalDateTime endTime, BigDecimal cost) {
        this.id = id;
        this.userId = userId;
        this.stationId = stationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
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

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
