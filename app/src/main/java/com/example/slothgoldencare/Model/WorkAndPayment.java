package com.example.slothgoldencare.Model;

import com.google.firebase.Timestamp;

public class WorkAndPayment {
    private Timestamp dateDay;
    private String doctorId;
    private String hours;
    private String isPaid;
    private Timestamp paidDate;

    public WorkAndPayment() {
    }

    // Constructor with all fields
    public WorkAndPayment(Timestamp dateDay, String doctorId, String hours, String isPaid, Timestamp paidDate) {
        this.dateDay = dateDay;
        this.doctorId = doctorId;
        this.hours = hours;
        this.isPaid = isPaid;
        this.paidDate = paidDate;
    }
    // Getters
    public Timestamp getDateDay() {
        return dateDay;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getHours() {
        return hours;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public Timestamp getPaidDate() {
        return paidDate;
    }

    // Setters
    public void setDateDay(Timestamp dateDay) {
        this.dateDay = dateDay;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setIsPaid(String paid) {
        isPaid = paid;
    }

    public void setPaidDate(Timestamp paidDate) {
        this.paidDate = paidDate;
    }
}
