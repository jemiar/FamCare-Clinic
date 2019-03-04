package com.project.hoangminh.clinicapp;

public class FamCareMessage {

    private String text;
    private String userName;

    public FamCareMessage() {
    }

    public FamCareMessage(String n, String t) {
        userName = n;
        text = t;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
