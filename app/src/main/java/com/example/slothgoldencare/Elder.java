package com.example.slothgoldencare;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Elder extends User{

    private Date DOB;
    private Gender gender;


    public Elder(String ID, String username, String phoneNumber, Date DOB,Gender gender) {
        super(ID, username, phoneNumber);
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

    public String formatDateOfBirth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Gender GenderConvertor(String gender){
        switch(gender){
            case "Female":
                return Gender.Female;
            case "Male":
                return Gender.Male;
        }
        return null;
    }

}
