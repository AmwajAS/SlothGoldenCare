package com.example.slothgoldencare.Model;

public class Medicine {

    private String elderlyDocId;
    private String medicine;

    public Medicine(String elderlyDocId, String medicine) {
        this.elderlyDocId = elderlyDocId;
        this.medicine = medicine;
    }

    public String getElderlyDocId() {
        return elderlyDocId;
    }

    public void setElderlyDocId(String elderlyDocId) {
        this.elderlyDocId = elderlyDocId;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
}
