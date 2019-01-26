package com.project.hoangminh.clinicapp;

public class Vital_Item {
    private String label;
    private String low_btn_txt;
    private String normal_btn_txt;
    private String hi_btn_txt;
    private boolean is_two_btns;

    public Vital_Item(String l, String low, String norm, String hi, boolean b) {
        label = l;
        low_btn_txt = low;
        normal_btn_txt = norm;
        hi_btn_txt = hi;
        is_two_btns = b;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLow_btn_txt(String low_btn_txt) {
        this.low_btn_txt = low_btn_txt;
    }

    public void setNormal_btn_txt(String normal_btn_txt) {
        this.normal_btn_txt = normal_btn_txt;
    }

    public void setHi_btn_txt(String hi_btn_txt) {
        this.hi_btn_txt = hi_btn_txt;
    }

    public String getLabel() {
        return label;
    }

    public String getLow_btn_txt() {
        return low_btn_txt;
    }

    public String getNormal_btn_txt() {
        return normal_btn_txt;
    }

    public String getHi_btn_txt() {
        return hi_btn_txt;
    }

    public boolean get_btn_no() {
        return is_two_btns;
    }
}
