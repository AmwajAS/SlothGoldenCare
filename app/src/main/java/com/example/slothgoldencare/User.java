package com.example.slothgoldencare;

public class User {
    private String username;
    private String ID;
    private String phoneNumber;


    public User(String username, String ID, String phoneNumber) {
        this.username = username;
        this.ID = ID;
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

