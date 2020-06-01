package com.example.timesheetapplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public  class security {


    public static String generateotp() {
        return (Integer.parseInt(new SimpleDateFormat("HHmm", Locale.getDefault()).format(new Date())) * 2 + 87 + 78) + "";
    }
}
