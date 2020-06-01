package com.example.timesheetapplication;

import android.app.Application;

import com.firebase.client.Firebase;

public class FirebaseContext  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);


    }
}
