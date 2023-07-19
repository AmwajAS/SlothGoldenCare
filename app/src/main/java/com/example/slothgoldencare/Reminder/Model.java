package com.example.slothgoldencare.Reminder;


/**
 * The Model class is a data model used to set and get data from the database.
 */


public class Model {
    String title, date, time;

    /**
     * Default constructor for the Model class.
     */
    public Model() {
    }

    /**
     * Constructor for the Model class.
     *
     * @param title The title of the model.
     * @param date  The date of the model.
     * @param time  The time of the model.
     */
    public Model(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    /*
    getters & setters
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}