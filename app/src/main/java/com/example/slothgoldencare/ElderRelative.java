package com.example.slothgoldencare;

public class ElderRelative {
    String relativeId;
    String elderlyId;
    String relation;
    String docId;

    public ElderRelative(String relativeId, String elderlyId, String relation) {
        this.relativeId = relativeId;
        this.elderlyId = elderlyId;
        this.relation = relation;
    }

    public String getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(String relativeId) {
        this.relativeId = relativeId;
    }

    public String getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(String elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setDocId(String docId){
        this.docId = docId;
    }

    public String getDocId(){
        return docId;
    }
}
