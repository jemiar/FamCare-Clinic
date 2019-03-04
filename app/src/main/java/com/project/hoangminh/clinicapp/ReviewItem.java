package com.project.hoangminh.clinicapp;

public class ReviewItem {
    private String label;
    private String val;

    public ReviewItem(String l, String v) {
        label = l;
        val = v;
    }

    //Note: Need to have default constructor and getter methods
    //otherwise, app will crash
    public ReviewItem() {
        label = "nah";
        val = "nah";
    }

    public String getLabel() {
        return label;
    }

    public String getVal() {
        return val;
    }
}
