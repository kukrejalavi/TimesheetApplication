package com.example.timesheetapplication.Admin.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.timesheetapplication.R;

public class RejectReason extends AppCompatActivity {
EditText txtrsn;
Button savebtn;
public int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_reason);


        txtrsn = findViewById(R.id.txtreason);
        savebtn = findViewById(R.id.Savebtn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent returnInt = new Intent();
returnInt.putExtra("result",txtrsn.getText().toString());
returnInt.putExtra("position",position);
                setResult(RESULT_OK, returnInt);
finish();
            }
        });
    }
}
