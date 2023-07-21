package com.example.slothgoldencare;

import java.util.Date;

public class Surgery {
    private String elderlyDocId;
    private String surgery;
    private Date date;

    public Surgery(String elderlyDocId, String surgery, Date date) {
        this.elderlyDocId = elderlyDocId;
        this.surgery = surgery;
        this.date = date;
    }

    public String getElderlyDocId() {
        return elderlyDocId;
    }

    public void setElderlyDocId(String elderlyDocId) {
        this.elderlyDocId = elderlyDocId;
    }

    public String getSurgery() {
        return surgery;
    }

    public void setSurgery(String surgery) {
        this.surgery = surgery;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
