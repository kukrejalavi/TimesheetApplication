package com.example.timesheetapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timesheetapplication.Email.SendMail;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.Security;

import javax.mail.MessagingException;

public class ResetOptions extends AppCompatActivity {
    TextView txtpasshint;
    Button sendotpbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_options);

        txtpasshint = findViewById(R.id.passwordhint);
        sendotpbtn = findViewById(R.id.sendotpbtn);


        final Intent intent = getIntent();
        txtpasshint.setText(intent.getStringExtra("passhint"));
final String name = intent.getStringExtra("name");

        sendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!InternetConnection.isNetworkAvailable(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetOptions.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please Check Your Internet Connection");
                        // add a button
                        builder.setPositiveButton("Dismiss", null);
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(intent.getStringExtra("emailid")).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {


                                    final String id = Firebaselinks.otpfirebaselink().push().getKey();
                                    Firebaselinks.otpfirebaselink().child(id).child("date").setValue(SetDate.Currentdateinyyyymmdd());
                                    Firebaselinks.otpfirebaselink().child(id).child("emailid").setValue(intent.getStringExtra("emailid"));
                                    Firebaselinks.otpfirebaselink().child(id).child("otp").setValue(security.generateotp());


                                    StrictMode.ThreadPolicy policy = new
                                            StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    try {
                                        SendMail.sendPlainTextEmail("smtp.office365.com", "587", "Lavina.Kukreja@revolvespl.com", "Kaavya786@", intent.getStringExtra("emailid"), "Reset Password", "Hi " + name + "," + "\r\n" + "Please find your OTP to reset password for Revolve's time App:" + "\r\n" + security.generateotp());
                                    } catch (MessagingException e) {
                                        e.printStackTrace();
                                    }


                                    Intent intent1 = new Intent(getApplicationContext(), EnterOtp.class);
                                    intent1.putExtra("emailid", intent.getStringExtra("emailid"));
                                    startActivity(intent1);


                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                }catch(Exception e){}
            }
        });
    }
}
