package com.example.timesheetapplication;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Shortcuts {

    public static Boolean childgetvalueequals(DataSnapshot child,String dbname,String equalto){
        return child.child(dbname).getValue().toString().equals(equalto);
    }


    public static String childgetvalue(DataSnapshot child,String dbname){
        return child.child(dbname).getValue().toString();
    }


    public void opentimedialog(final EditText editText, Context ctx){
        final List<Float> totalhrs = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        final TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = "";

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);
                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                String minutes = String.valueOf(datetime.get(Calendar.MINUTE));
//                                if (minutes.length() == 1) {
//                                    minutes = "0" + (Integer.parseInt(minutes) + 1);
//                                } else {
                minutes = String.valueOf(datetime.get(Calendar.MINUTE));
                //}
//
//


                editText.setText(strHrsToShow + "." + minutes);

            }


        };
        new TimePickerDialog(ctx, time_listener, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), true).show();
    }

}
