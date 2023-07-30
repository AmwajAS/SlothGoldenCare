package com.example.slothgoldencare.Model;

public class WorkAndPayment {
    private String dateDay;
    private String doctorId;
    private String hours;
    private boolean isPaid;
    private String paidDate;

    public WorkAndPayment() {
    }
    // Constructor
    public WorkAndPayment(String dateDay, String doctorId, String hours, boolean isPaid, String paidDate) {
        this.dateDay = dateDay;
        this.doctorId = doctorId;
        this.hours = hours;
        this.isPaid = isPaid;
        this.paidDate = paidDate;
    }

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
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

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }
}
