package com.example.mobilesensorapplication.OperationalClasses;

import java.util.Date;

public class SystemDateTime {
    static String recordingStartTime = "000000";

    public static String getDate() {
        android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat("yyyy:MM:dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String geTime() {
        android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat("HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String geTimeForCSV() {
        android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        recordingStartTime= currentDateandTime;
        return currentDateandTime;
    }

}
