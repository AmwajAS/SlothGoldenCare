package com.example.slothgoldencare.Model;

import com.google.firebase.Timestamp;

public class Appointment {
    private Timestamp date;
    private String elderId;
    private String notes;
    private String doctorId;

    public Appointment(Timestamp date, String elderId, String notes, String doctorId) {
        this.date = date;
        this.elderId = elderId;
        this.notes = notes;
        this.doctorId = doctorId;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


}