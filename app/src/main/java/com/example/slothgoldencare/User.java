package com.example.slothgoldencare;

public class User {
    private String ID;
    private String username;
    private int imgID;

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public int getImgID() {
        return imgID;
    }

    private String phoneNumber;


    public User(String ID, String username, String phoneNumber) {
        this.ID = ID;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

