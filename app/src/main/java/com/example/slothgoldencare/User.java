package com.example.slothgoldencare;

public class User {
    private String username;
    private int ID;
    private long phoneNumber;


    public User(String username, int ID, long phoneNumber) {
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

