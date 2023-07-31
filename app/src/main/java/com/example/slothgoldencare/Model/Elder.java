package com.example.slothgoldencare.Model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Elder extends User {

    private Date DOB;
    private Gender gender;


    public Elder(String ID, String username, String phoneNumber, Date DOB,Gender gender) {
        super(ID, username, phoneNumber);
        this.DOB = DOB;
        this.gender = gender;
    }
    public Elder(String ID, String username, String phoneNumber,Gender gender) {
        super(ID, username, phoneNumber);
        this.gender = gender;
    }

    public Elder(String ID, String username, String phoneNumber, Date DOB,Gender gender,String email,String password) {
        super(ID, username, phoneNumber,email,password);
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

    public String getID() {
        return "" + super.getID() + " " + getUsername() + "";
    }


    public static Date convertStringIntoDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateString);
            // Your code to handle the parsed date
        } catch (ParseException e) {
            // Handle the case where the input is not a valid date
        }
        return date;
    }

/*
this method convert from String to Gender - Enum
 */
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
