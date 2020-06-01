package com.example.timesheetapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {
EditText txtemailid;
Button nextbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        txtemailid = findViewById(R.id.emailid);
        nextbtn = findViewById(R.id.nextbtn);

//        txtemailid.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//if(!s.toString().isEmpty()){
//    nextbtn.setEnabled(true);
//}
//else
//    nextbtn.setEnabled(false);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {



                    if (!InternetConnection.isNetworkAvailable(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please Check Your Internet Connection");
                        // add a button
                        builder.setPositiveButton("Dismiss", null);
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {

                        if (!txtemailid.getText().toString().isEmpty()) {


                            Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(txtemailid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0) {

                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                            Intent intent = new Intent(getApplicationContext(), ResetOptions.class);
                                            intent.putExtra("emailid", txtemailid.getText().toString());
                                            intent.putExtra("name", child.child("name").getValue().toString());
                                            intent.putExtra("passhint", child.child("passwordhint").getValue().toString());
                                            startActivity(intent);
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Emailid does not exists", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }else
                            Validations.Toast(getApplicationContext(),"Please enter emailid");
                    }
                } catch (Exception e) {
                }
            }
        });





    }
}
