package com.example.slothgoldencare;

public class Allergy {

    private String elderlyDocId;
    private String Allergy;

    public Allergy(String elderlyDocId, String allergy) {
        this.elderlyDocId = elderlyDocId;
        Allergy = allergy;
    }

    public String getElderlyDocId() {
        return elderlyDocId;
    }

    public void setElderlyDocId(String elderlyDocId) {
        this.elderlyDocId = elderlyDocId;
    }

    public String getAllergy() {
        return Allergy;
    }

    public void setAllergy(String allergy) {
        Allergy = allergy;
    }
}
