package com.example.timesheetapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.SettingInjectorService;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timesheetapplication.Admin.java.AdminNavigation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    TextInputEditText txtusername,txtpassword;
    Button loginbtn;
    TextView txtforgotpassword;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;

    public static final String PREFS_NAME = "LoginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      txtusername = findViewById(R.id.username);
       txtpassword = findViewById(R.id.password);
        txtforgotpassword = findViewById(R.id.forgotpassword);

        loginbtn = findViewById(R.id.loginbtn);


        txtusername.setText("nilesh.peswani@revolvespl.com");
        txtpassword.setText("a");

        txtforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                try{
                    if (!InternetConnection.isNetworkAvailable(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please Check Your Internet Connection");
                        // add a button
                        builder.setPositiveButton("Dismiss", null);
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                        startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_SHORT).show();
                }
            }
        });



        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
                Intent intent = new Intent(Login.this, AdminNavigation.class);
                startActivity(intent);


        }
        if (settings.getString("emplogged", "").toString().equals("emplogged")) {
            Intent intent = new Intent(Login.this, EmployeeMainPage.class);
            startActivity(intent);
        }



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {

                    // Call isNetworkAvailable class
                    if (!InternetConnection.isNetworkAvailable(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please Check Your Internet Connection");
                        // add a button
                        builder.setPositiveButton("Dismiss", null);
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                  else{
                        if (!txtusername.getText().toString().isEmpty()) {
                            if (!txtpassword.getText().toString().isEmpty()) {

                                Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(txtusername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() > 0) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                if (child.child("status").getValue().toString().equals("Active")) {
                                                    if (child.child("password").getValue().toString().equals(txtpassword.getText().toString())) {
                                                        if (child.child("profile").getValue().toString().equals("Admin")) {
                                                            Intent intent = new Intent(getApplicationContext(), AdminNavigation.class);
                                                            intent.putExtra("id", child.child("id").getValue().toString());
                                                            intent.putExtra("name", child.child("name").getValue().toString());
                                                            intent.putExtra("emailid", child.child("emailid").getValue().toString());
                                                            intent.putExtra("number", child.child("number").getValue().toString());
                                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                                            SharedPreferences.Editor editor = settings.edit();
                                                            editor.putString("logged", "logged");
                                                            editor.putString("id", child.child("id").getValue().toString());
                                                            editor.putString("name", child.child("name").getValue().toString());
                                                            editor.putString("emailid", child.child("emailid").getValue().toString());
                                                            editor.putString("number", child.child("number").getValue().toString());
                                                            editor.commit();


                                                            progressbar(v);
                                                            finish();
                                                            startActivity(intent);

                                                        }
                                                        if (child.child("profile").getValue().toString().equals("Supervisor")) {
                                                            Intent intent = new Intent(getApplicationContext(), AdminNavigation.class);
                                                            intent.putExtra("id", child.child("id").getValue().toString());
                                                            intent.putExtra("name", child.child("name").getValue().toString());
                                                            intent.putExtra("emailid", child.child("emailid").getValue().toString());
                                                            intent.putExtra("number", child.child("number").getValue().toString());
                                                            intent.putExtra("supervisor","supervisor");

                                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                                            SharedPreferences.Editor editor = settings.edit();
                                                            editor.putString("logged", "logged");
                                                            editor.putString("id", child.child("id").getValue().toString());
                                                            editor.putString("name", child.child("name").getValue().toString());
                                                            editor.putString("emailid", child.child("emailid").getValue().toString());
                                                            editor.putString("number", child.child("number").getValue().toString());
                                                            editor.putString("supervisor", "supervisor");
                                                            editor.commit();
                                                            progressbar(v);
                                                            finish();

                                                            startActivity(intent);

                                                        }
                                                        if (child.child("profile").getValue().toString().equals("Employee")) {
                                                            Intent intent = new Intent(getApplicationContext(), EmployeeMainPage.class);
                                                            intent.putExtra("id", child.child("id").getValue().toString());
                                                            intent.putExtra("name", child.child("name").getValue().toString());
                                                            intent.putExtra("emailid", child.child("emailid").getValue().toString());
                                                            intent.putExtra("number", child.child("number").getValue().toString());
                                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                                            SharedPreferences.Editor editor = settings.edit();
                                                            editor.putString("emplogged", "emplogged");
                                                            editor.putString("id", child.child("id").getValue().toString());
                                                            editor.putString("name", child.child("name").getValue().toString());
                                                            editor.putString("emailid", child.child("emailid").getValue().toString());
                                                            editor.putString("number", child.child("number").getValue().toString());
                                                            editor.commit();
                                                            progressbar(v);
                                                            finish();

                                                            startActivity(intent);

                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Emailid has been deactivated", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            } else {
                                txtpassword.setError("Please Enter Password");
                            }
                        } else {
                            txtusername.setError("Please Enter Username");
                        }
                    }
                }catch (Exception e){
                }
            }
        });


    }

    public void ShowHidePass(View view){

        if(view.getId()==R.id.showhidebtn){

            if(txtpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide);

                //Show Password
                txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_show);

                //Hide Password
                txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }


    public void progressbar(View v){
        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
//        progressBar.show();
        //reset progress bar and filesize status
        progressBarStatus = 0;
        fileSize = 0;


        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    progressBarStatus = downloadFile();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }
            }
        }).start();








    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(progressBar!= null){
            progressBar.dismiss();
        }
    }

    public int downloadFile() {
        while (fileSize <= 1000000) {
            fileSize++;

            if (fileSize == 100000) {
                return 10;
            }else if (fileSize == 200000) {
                return 20;
            }else if (fileSize == 300000) {
                return 30;
            }else if (fileSize == 400000) {
                return 40;
            }else if (fileSize == 500000) {
                return 50;
            }else if (fileSize == 700000) {
                return 70;
            }else if (fileSize == 800000) {
                return 80;
            }
        }
        return 100;
    }
}
