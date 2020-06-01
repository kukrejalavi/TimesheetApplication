package com.example.timesheetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timesheetapplication.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class AddCostCenter extends AppCompatActivity {
    EditText txthint;
    TextView txtheader;
    Button savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost_center);


        txtheader = findViewById(R.id.txtheader);
        txthint = findViewById(R.id.txthint);
        savebtn = findViewById(R.id.Savebtn);


        Intent intent = getIntent();
        //addcostcenter and addcategory
        if(intent.getStringExtra("fragmenttype").equals("Costcenter")){
txtheader.setText("Add Cost Center");
            txthint.setHint("Please Enter Cost Center");


            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!txthint.getText().toString().isEmpty()) {

                        Firebaselinks.costcenterfirebaselink().orderByChild("costcenter").equalTo(txthint.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount() > 0){

                                    Validations.Toast(getApplicationContext(),"Cost center already exists");
                                    return;
                                }
                                else{

                                    Intent returnInt = new Intent();
                                    returnInt.putExtra("result", txthint.getText().toString());
                                    setResult(RESULT_OK, returnInt);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });




                    }
                    else
                        Validations.Toast(getApplicationContext(),"Please enter cost center");
                }
            });
        }
        if(intent.getStringExtra("fragmenttype").equals("Category")){
            txtheader.setText("Add Category");
            txthint.setHint("Please Enter Category");



            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!txthint.getText().toString().isEmpty()) {

                        Firebaselinks.costcenterfirebaselink().orderByChild("costcenter").equalTo(txthint.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount() > 0){

                                    Validations.Toast(getApplicationContext(),"Cost center already exists");
                                    return;
                                }
                                else{

                                    Intent returnInt = new Intent();
                                    returnInt.putExtra("result", txthint.getText().toString());
                                    setResult(RESULT_OK, returnInt);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });




                    }
                    else
                        Validations.Toast(getApplicationContext(),"Please enter cost center");
                }
            });



        }

    }
}
