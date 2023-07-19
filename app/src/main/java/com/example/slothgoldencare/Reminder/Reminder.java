package com.example.slothgoldencare.Reminder;

//model class is used to set and get the data from the database

import java.util.Date;

public class Reminder {
    String elderlyDocId,title, time;
    Date date;

    public Reminder() {
    }

    public Reminder(String elderlyDocId,String title, Date date, String time) {
        this.elderlyDocId = elderlyDocId;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getElderlyDocId() {
        return elderlyDocId;
    }

    public void setElderlyDocId(String elderlyDocId) {
        this.elderlyDocId = elderlyDocId;
    }
}