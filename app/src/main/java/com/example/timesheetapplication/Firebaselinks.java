package com.example.timesheetapplication;

import com.firebase.client.Firebase;

public class Firebaselinks {
   // testing
 static  String url = "https://timesheetapplication-2ec0b.firebaseio.com";
    //live
//static  String url = "https://timesheetapplication-1568.firebaseio.com";
    static Firebase assignsvtoempfirebase,projectsfirebase,loginfirebase,timesheetfirebase,myaccountfirebase,assignedprojectsfirebase,totalhrsbydatetimesheetfirebase;
    static Firebase notificationsfirebase,otpfirebase,costcenterfirebase,adminfirebase,paytypefirebase,projectcategoryfirebase,calendarfirebase;

    public static Firebase projectcategoryfirebaselink() {
        projectcategoryfirebase = new Firebase(url + "/ProjectCategory");
        return projectcategoryfirebase;
    }

    public static Firebase calendarfirebaselink() {
        calendarfirebase = new Firebase(url + "/Calendar");
        return calendarfirebase;
    }




    public static Firebase paytypefirebaselink() {
        paytypefirebase = new Firebase(url + "/PayType");
        return paytypefirebase;
    }
    public static Firebase adminfirebaselink() {
        adminfirebase = new Firebase(url + "/Admins");
        return adminfirebase;
    }
    public static Firebase projectsfirebaselink() {
        projectsfirebase = new Firebase(url + "/Projects");
        return projectsfirebase;
    }
    public static Firebase costcenterfirebaselink() {
        costcenterfirebase = new Firebase(url + "/Costcenter");
        return costcenterfirebase;
    }

    public static Firebase loginfirebaselink() {
        loginfirebase = new Firebase(url + "/Login");
        return loginfirebase;
    }
    public static Firebase assignsvtoemp() {
        assignsvtoempfirebase = new Firebase(url + "/Assignsvtoemp");
        return assignsvtoempfirebase;
    }

    public static Firebase timesheetfirebaselink() {
        timesheetfirebase = new Firebase(url + "/Timesheet");
        return timesheetfirebase;
    }


    public static Firebase totalhrsbydatetimesheetfirebaselink() {
        totalhrsbydatetimesheetfirebase = new Firebase(url + "/TotalhrsbydateTimesheet");
        return totalhrsbydatetimesheetfirebase;
    }

    public static Firebase myaccountfirebaselink() {
        myaccountfirebase = new Firebase(url + "/MyAccounts");
        return myaccountfirebase;
    }

    public static Firebase assignedprojectsfirebaselink() {
        assignedprojectsfirebase = new Firebase(url + "/Assigned Projects");
        return assignedprojectsfirebase;
    }

    public static Firebase notificationsfirebaselink() {
        notificationsfirebase = new Firebase(url + "/Notifications");
        return notificationsfirebase;
    }

    public static Firebase otpfirebaselink() {
        otpfirebase = new Firebase(url + "/OTP");
        return otpfirebase;
    }
}
