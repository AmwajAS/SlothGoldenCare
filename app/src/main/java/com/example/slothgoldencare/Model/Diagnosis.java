package com.example.slothgoldencare.Model;

public class Diagnosis {
    private String elderlyDocId;
    private String diagnosis;

    public Diagnosis(String elderlyDocId, String diagnosis) {
        this.elderlyDocId = elderlyDocId;
        this.diagnosis = diagnosis;
    }

    public String getElderlyDocId() {
        return elderlyDocId;
    }

    public void setElderlyDocId(String elderlyDocId) {
        this.elderlyDocId = elderlyDocId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
