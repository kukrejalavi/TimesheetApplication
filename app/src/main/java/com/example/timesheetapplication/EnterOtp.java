package com.example.timesheetapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EnterOtp extends AppCompatActivity {
EditText txtotp;
Button donebtn;
LinearLayout llt;
EditText txtpassword,txtrepeatpass,txtpasshint;
Button savebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        txtotp = findViewById(R.id.txtotp);
        donebtn = findViewById(R.id.donebtn);

llt = findViewById(R.id.llt);

        txtpassword = findViewById(R.id.newpassword);
        txtrepeatpass = findViewById(R.id.repeatpassword);
        txtpasshint = findViewById(R.id.passwordhint);
        savebtn = findViewById(R.id.savebtn);

Intent intent = getIntent();
final String emailid=intent.getStringExtra("emailid");

donebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!InternetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EnterOtp.this);
            builder.setTitle("Error");
            builder.setMessage("Please Check Your Internet Connection");
            // add a button
            builder.setPositiveButton("Dismiss", null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            if (!txtotp.getText().toString().isEmpty()) {
                Firebaselinks.otpfirebaselink().orderByChild("otp").equalTo(txtotp.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            txtotp.setEnabled(false);
                            donebtn.setEnabled(false);


                            llt.setVisibility(View.VISIBLE);

                        } else {
                            Validations.Toast(getApplicationContext(), "Invalid OTP");
                        }


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            } else {
                Validations.Toast(getApplicationContext(), "Please Enter OTP");
            }
        }
    }
});





savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!InternetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EnterOtp.this);
            builder.setTitle("Error");
            builder.setMessage("Please Check Your Internet Connection");
            // add a button
            builder.setPositiveButton("Dismiss", null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (!txtpassword.getText().toString().isEmpty() && !txtrepeatpass.getText().toString().isEmpty() && !txtpasshint.getText().toString().isEmpty()) {
                if (txtpassword.getText().toString().equals(txtrepeatpass.getText().toString())) {
                    Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(emailid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String clubkey = child.getKey();
                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/password").setValue(txtpassword.getText().toString());
                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/passwordhint").setValue(txtpasshint.getText().toString());

                                }
                                Validations.Toast(getApplicationContext(), "Successfully Changed Password");
                                Intent intent = new Intent(EnterOtp.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else {
                    Validations.Toast(getApplicationContext(), "Passwords does not match");
                    return;
                }
            } else {
                Validations.Toast(getApplicationContext(), "Please enter all fields");
            }
        }
    }
});



    }
}
