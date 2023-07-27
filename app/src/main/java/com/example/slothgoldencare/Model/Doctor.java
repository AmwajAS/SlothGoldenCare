package com.example.slothgoldencare.Model;

public class Doctor {


    private String docId;
    private int imgID;
    private String specialization;
    private String email;
    private String password;
    private String ID;
    private String username;
    private String phoneNumber;


    public Doctor(String ID, String username,  String phoneNumber , String email, String password, String specialization) {
        this.ID = ID;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDocId() {
        return docId;
    }

    public int getImgID() {
        return imgID;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }
}
