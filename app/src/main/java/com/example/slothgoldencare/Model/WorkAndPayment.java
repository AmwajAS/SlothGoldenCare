package com.example.slothgoldencare.Model;

import com.google.firebase.Timestamp;

public class WorkAndPayment {
    private Timestamp dateDay;
    private String doctorId;
    private String hours;
    private boolean isPaid;
    private Timestamp paidDate;

    public WorkAndPayment() {
    }

    // Constructor with all fields
    public WorkAndPayment(Timestamp dateDay, String doctorId, String hours, boolean isPaid, Timestamp paidDate) {
        this.dateDay = dateDay;
        this.doctorId = doctorId;
        this.hours = hours;
        this.isPaid = isPaid;
        this.paidDate = paidDate;
    }

    // Getters and Setters
    public Timestamp getDateDay() {
        return dateDay;
    }

    public void setDateDay(Timestamp dateDay) {
        this.dateDay = dateDay;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Timestamp getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Timestamp paidDate) {
        this.paidDate = paidDate;
    }

}
