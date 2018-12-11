package com.example.abhishekaryan.notetaker.Entities;

public class DataEntity {

    private String noteText;
    private String noteCreatedOn;

    public DataEntity(String noteText, String noteCreatedOn) {
        this.noteText = noteText;
        this.noteCreatedOn = noteCreatedOn;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getNoteCreatedOn() {
        return noteCreatedOn;
    }
}




