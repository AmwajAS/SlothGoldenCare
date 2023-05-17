package com.example.slothgoldencare;

import java.util.Date;

public class Elder extends User{

    private Date DOB;
    private Gender gender;


    public Elder(String username, int ID, long phoneNumber, Date DOB, Gender gender) {
        super(username, ID, phoneNumber);
        this.DOB = DOB;
        this.gender = gender;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
