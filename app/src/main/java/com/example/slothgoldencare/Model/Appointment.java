package com.example.slothgoldencare.Model;

import com.google.firebase.Timestamp;

public class Appointment {
    private Timestamp date;
    private String elderId;
    private String notes;
    private String doctor;

    public Appointment() {
        // Required empty constructor for Firebase Firestore deserialization
    }
    public Appointment(Timestamp date, String elderId, String notes, String doctor) {
        this.date = date;
        this.elderId = elderId;
        this.notes = notes;
        this.doctor = doctor;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getElderId() {
        return elderId;
    }

    public void setElderId(String elderId) {
        this.elderId = elderId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
