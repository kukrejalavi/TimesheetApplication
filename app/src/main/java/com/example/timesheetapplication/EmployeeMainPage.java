package com.example.timesheetapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timesheetapplication.Admin.java.AddnewProject;
import com.example.timesheetapplication.Admin.java.Profile;
import com.example.timesheetapplication.Employee.java.AddTimesheetMainPage;
import com.example.timesheetapplication.Employee.java.FragmentNotificatioins;
import com.example.timesheetapplication.Employee.java.viewprojects;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EmployeeMainPage extends AppCompatActivity {
    LinearLayout dashboardllt,projectllt,notificationsllt,editprofilellt,Calendarllt;
    TextView txtdashboard,txtprojects,txtnotifications,txteditprofile,txtcalendar;
    ImageView imgdashboard,imgnotification,imgproject,imgreport,imgcalendar;
String empid,empname;
    ImageButton logoutbtn;
int notifycount;
TextView txtcount;
    public static final String PREFS_NAME = "LoginPrefs";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main_page);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Timesheet");
            toolbar.setBackgroundColor(Color.rgb(72, 120, 170));
            logoutbtn = findViewById(R.id.logoutbtn);

            Intent intent = getIntent();
            empid = intent.getStringExtra("id");
            empname = intent.getStringExtra("name");


            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            if (settings != null) {
                empid = settings.getString("id", "");
                empname = settings.getString("name", "");
                //  empid =    settings.getString("emailid","");
                //empid =    settings.getString("number","");
            }


            txtcount = findViewById(R.id.count);

            dashboardllt = findViewById(R.id.Dashboard);
            projectllt = findViewById(R.id.Projects);
            notificationsllt = findViewById(R.id.Notifications);
            editprofilellt = findViewById(R.id.Editprofile);
            Calendarllt = findViewById(R.id.Calendar);


//        txtdashboard = findViewById(R.id.dashboard);
//        txtprojects = findViewById(R.id.projects);
//        txtnotifications = findViewById(R.id.notifications);
//        txteditprofile = findViewById(R.id.editprofile);
            // txtleave = findViewById(R.id.leave);

            imgdashboard = findViewById(R.id.imgdashboard);
            imgnotification = findViewById(R.id.imgnotifications);
            imgproject = findViewById(R.id.imgprojects);
            imgreport = findViewById(R.id.imgeditprofile);
            imgcalendar = findViewById(R.id.imgCalendar);


            notificationcount();

            imgdashboard.setColorFilter(Color.rgb(72, 120, 170));
            //  txtdashboard.setTextColor(Color.rgb(72,120,170));
            imgproject.setColorFilter(Color.rgb(80, 80, 80));
            //   txtprojects.setTextColor(Color.rgb(80,80,80));
            imgnotification.setColorFilter(Color.rgb(80, 80, 80));
            //  txtnotifications.setTextColor(Color.rgb(80,80,80));
            imgreport.setColorFilter(Color.rgb(80, 80, 80));
            //  txteditprofile.setTextColor(Color.rgb(80,80,80));
            imgcalendar.setColorFilter(Color.rgb(80, 80, 80));
            //  txtleave.setTextColor(Color.rgb(80,80,80));

            FragmentManager fragmentManager = getSupportFragmentManager();
            AddTimesheetMainPage addTimesheetMainPage = new AddTimesheetMainPage();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, addTimesheetMainPage).commit();
            addTimesheetMainPage.empid = empid;
            addTimesheetMainPage.empname = empname;


            logoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("emplogged");
                    editor.commit();
                    finish();

                    Intent intent = new Intent(EmployeeMainPage.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }catch(Exception e){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void notificationcount(){
    final int[] counter = {0};
    Firebaselinks.notificationsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0){
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("empid").getValue().toString() != null && child.child("status").getValue().toString().equals("") ) {
                        if (child.child("empid").getValue().toString().contains(",")) {

                            String h = child.child("empid").getValue().toString();

                            String[] f = h.split(",");


                            for (int i = 0; i < f.length; i++) {
                                if (f[i].contains(" ")) {
                                    f[i] = f[i].replace(" ", "");
                                }
                                if (f[i].equals(empid)) {

                                    counter[0]++;
                                }
                            }
                        } else {
                            if (child.child("empid").getValue().toString().equals(empid)) {
                                counter[0]++;
                            }
                        }
                    }
                }
            }

            notifycount = counter[0];
            if(notifycount >0) {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText(notifycount + "");

            }

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });
}
    public void bottombtnclicked(View view) {

        if(view.getId() == R.id.Dashboard){
            imgdashboard.setColorFilter(Color.rgb(72,120,170));
            //    txtdashboard.setTextColor(Color.rgb(72,120,170));
            imgproject.setColorFilter(Color.rgb(80,80,80));
            //    txtprojects.setTextColor(Color.rgb(80,80,80));
            imgnotification.setColorFilter(Color.rgb(80,80,80));
            //  txtnotifications.setTextColor(Color.rgb(80,80,80));
            imgreport.setColorFilter(Color.rgb(80,80,80));
            //  txteditprofile.setTextColor(Color.rgb(80,80,80));
            imgcalendar.setColorFilter(Color.rgb(80,80,80));
            //  txtleave.setTextColor(Color.rgb(80,80,80));

            getSupportActionBar().setTitle("Timesheet");
            FragmentManager fragmentManager = getSupportFragmentManager();
            AddTimesheetMainPage addTimesheetMainPage = new AddTimesheetMainPage();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, addTimesheetMainPage).commit();
            addTimesheetMainPage.empid = empid;
            addTimesheetMainPage.empname = empname;

        }

        else  if(view.getId() == R.id.Projects){

            imgproject.setColorFilter(Color.rgb(72,120,170));
            //txtprojects.setTextColor(Color.rgb(72,120,170));

            imgdashboard.setColorFilter(Color.rgb(80,80,80));
            //    txtdashboard.setTextColor(Color.rgb(80,80,80));
            imgnotification.setColorFilter(Color.rgb(80,80,80));
            //    txtnotifications.setTextColor(Color.rgb(80,80,80));
            imgreport.setColorFilter(Color.rgb(80,80,80));
            //  txteditprofile.setTextColor(Color.rgb(80,80,80));
            imgcalendar.setColorFilter(Color.rgb(80,80,80));
            // txtleave.setTextColor(Color.rgb(80,80,80));


      getSupportActionBar().setTitle("Timesheets");
            FragmentManager fragmentManager = getSupportFragmentManager();
            viewprojects Viewprojects = new viewprojects();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, Viewprojects).commit();
            Viewprojects.empid = empid;
            Viewprojects.empname = empname;

        }
        else  if(view.getId() == R.id.Notifications){

            imgnotification.setColorFilter(Color.rgb(72,120,170));
            //    txtnotifications.setTextColor(Color.rgb(72,120,170));

            imgdashboard.setColorFilter(Color.rgb(80,80,80));
            //  txtdashboard.setTextColor(Color.rgb(80,80,80));
               imgproject.setColorFilter(Color.rgb(80,80,80));
            //   txtprojects.setTextColor(Color.rgb(80,80,80));
            imgreport.setColorFilter(Color.rgb(80,80,80));
            //    txteditprofile.setTextColor(Color.rgb(80,80,80));
            imgcalendar.setColorFilter(Color.rgb(80,80,80));
            //  txtleave.setTextColor(Color.rgb(80,80,80));

          getSupportActionBar().setTitle("Notifications");
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentNotificatioins fragmentNotificatioins = new FragmentNotificatioins();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragmentNotificatioins).commit();

            fragmentNotificatioins.notifycount = notifycount;


            Firebaselinks.notificationsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.child("empid").getValue().toString() != null && child.child("status").getValue().toString().equals("") ) {
                               String clubkey = child.getKey();
                               Firebaselinks.notificationsfirebaselink().child(clubkey + "/status").setValue("Viewed");
                            }
                        }

                        if(txtcount.getVisibility() == View.VISIBLE)
                            txtcount.setVisibility(View.GONE);
                        fragmentNotificatioins.empid = empid;
                        fragmentNotificatioins.empname = empname;
                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        else  if(view.getId() == R.id.Editprofile) {
            imgreport.setColorFilter(Color.rgb(72,120,170));
            //   txteditprofile.setTextColor(Color.rgb(72,120,170));


            imgdashboard.setColorFilter(Color.rgb(80,80,80));
            //    txtdashboard.setTextColor(Color.rgb(80,80,80));
            imgproject.setColorFilter(Color.rgb(80,80,80));
            //    txtprojects.setTextColor(Color.rgb(80,80,80));
            imgnotification.setColorFilter(Color.rgb(80,80,80));
            //    txtnotifications.setTextColor(Color.rgb(80,80,80));
            imgcalendar.setColorFilter(Color.rgb(80,80,80));
            //   txtleave.setTextColor(Color.rgb(80,80,80));


           getSupportActionBar().setTitle("Edit Profile");
            FragmentManager fragmentManager = getSupportFragmentManager();
            Profile editprofile = new Profile();
            editprofile.type = "Employee";
            editprofile.empid  =empid;
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, editprofile).commit();


        }

        else  if(view.getId() == R.id.Calendar) {


            imgcalendar.setColorFilter(Color.rgb(72,120,170));
            //   txtleave.setTextColor(Color.rgb(72,120,170));



            imgdashboard.setColorFilter(Color.rgb(80,80,80));
            //    txtdashboard.setTextColor(Color.rgb(80,80,80));
            imgproject.setColorFilter(Color.rgb(80,80,80));
            //   txtprojects.setTextColor(Color.rgb(80,80,80));
            imgnotification.setColorFilter(Color.rgb(80,80,80));
            // txtnotifications.setTextColor(Color.rgb(80,80,80));
            imgreport.setColorFilter(Color.rgb(80,80,80));
            //   txteditprofile.setTextColor(Color.rgb(80,80,80));


            getSupportActionBar().setTitle("Calendar");
            FragmentManager fragmentManager = getSupportFragmentManager();
            CalendarView calendarView = new CalendarView();
            calendarView.empname =empname;
            calendarView.empid  =empid;
            calendarView.logintype  = "";

            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, calendarView).commit();



        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.exit(0);
    }
}
