package com.project.hoangminh.clinicapp;

import java.util.Calendar;
import java.util.TimeZone;

public class NoteItem {
    private String time;
    private String content;

    public NoteItem(String c) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        time = "" + months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR)
                    + "  " + cal.get(Calendar.HOUR_OF_DAY) + " : " + cal.get(Calendar.MINUTE);
        content = c;
    }

    public NoteItem(String t, String c) {
        time = t;
        content = c;
    }

    public NoteItem() {};

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
