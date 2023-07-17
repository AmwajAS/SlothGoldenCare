package com.example.slothgoldencare;

public class User {
    private String docId;
    private String ID;
    private String username;
    private int imgID;
    private String email;
    private String password;

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
    public User(String ID, String username, String phoneNumber,String email,String password) {
        this.ID = ID;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
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

    public void setDocId(String documentId){
        docId = documentId;
    }
    public String getDocId(){
        return docId;
    }
}

