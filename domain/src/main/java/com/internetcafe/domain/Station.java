package com.internetcafe.domain;

import java.math.BigDecimal;

public class Station {
    private int id;
    private String name;
    private String specs;
    private StationStatus status;
    private BigDecimal hourlyRate;

    public Station(int id, String name, String specs, StationStatus status, BigDecimal hourlyRate) {
        this.id = id;
        this.name = name;
        this.specs = specs;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public StationStatus getStatus() {
        return status;
    }

    public void setStatus(StationStatus status) {
        this.status = status;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
