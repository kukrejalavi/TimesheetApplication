package com.example.timesheetapplication.Employee.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddTimesheet extends Fragment {
    Toolbar toolbar;
    TextView txtcountertext;
    TextView txtdate,txtprojname,txtprojdesc;
    Spinner txtpaytype;
    EditText txtnoofhrs,txtdesc;
Button savebtn,editbtn,deletebtn;
public String empname,empid,date;
String type,logintype;
View view;
    int tid;
    final List<String> paytypelist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.activity_add_timesheet, container, false);
        savebtn = view.findViewById(R.id.btn);
        editbtn = view.findViewById(R.id.editbtn);
        deletebtn = view.findViewById(R.id.deletebtn);

                txtdate = view.findViewById(R.id.date);
                txtprojname = view.findViewById(R.id.projname);
                txtprojdesc = view.findViewById(R.id.projdesc);
                txtpaytype = view.findViewById(R.id.paytype);
                txtnoofhrs = view.findViewById(R.id.addhrs);
        txtdesc = view.findViewById(R.id.desc);








        final Bundle bundle = this.getArguments();

        if (bundle != null) {
            txtprojname.setText(bundle.getString("projname"));
            txtprojdesc.setText(bundle.getString("projdesc"));

        }
        type = bundle.getString("type");

        if(type.equals("AllProjects")){
            tid = maxtimesheetid();
            savebtn.setVisibility(View.VISIBLE);
         date =    bundle.getString("date");

            Firebaselinks.paytypefirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                        if(dataSnapshot.getChildrenCount() > 0){
                            for(DataSnapshot child :dataSnapshot.getChildren()){
                                    paytypelist.add(child.child("paytype").getValue().toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                    android.R.layout.simple_spinner_item, paytypelist);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            txtpaytype.setAdapter(adapter);
                            txtpaytype.setSelection(0);
                        }
                    }catch(Exception e){}

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        if(type.equals("arrow")){
            txtnoofhrs.setText(bundle.getString("hrs"));
            txtdesc.setText(bundle.getString("desc"));
            tid =Integer.parseInt(bundle.getString("tid"));
            deletebtn.setVisibility(View.VISIBLE);
            editbtn.setVisibility(View.VISIBLE);
            savebtn.setVisibility(View.GONE);


            Firebaselinks.timesheetfirebaselink().orderByChild("timesheetid").equalTo(tid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            txtdesc.setText(child.child("desc").getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Firebaselinks.paytypefirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                    if(dataSnapshot.getChildrenCount() > 0){
                        paytypelist.add(bundle.getString("paytype"));
                        for(DataSnapshot child :dataSnapshot.getChildren()){
                            if(!paytypelist.contains(child.child("paytype").getValue().toString()))
                            paytypelist.add(child.child("paytype").getValue().toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, paytypelist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        txtpaytype.setAdapter(adapter);
                        txtpaytype.setSelection(0);
                    }
                    }catch(Exception e){}

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        }


        txtdate.setText(SetDate.withoutonclickDate(txtdate,getContext()));


savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        savemethod();


    }
});


editbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
editmethod();
    }
});

deletebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        deletemethod();
    }
});
return  view;

 }

public void editmethod(){
    Firebaselinks.timesheetfirebaselink().orderByChild("timesheetid").equalTo(tid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0){
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.child("empid").getValue().toString().equals(empid)) {
                        String clubkey = child.getKey();
                        Firebaselinks.timesheetfirebaselink().child(clubkey+"/noofhrs").setValue(txtnoofhrs.getText().toString());
                        Firebaselinks.timesheetfirebaselink().child(clubkey+"/desc").setValue(txtdesc.getText().toString());
                        Firebaselinks.timesheetfirebaselink().child(clubkey+"/paytype").setValue(txtpaytype.getSelectedItem().toString());
                    }
                }
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                AddTimesheetMainPage fragment = new AddTimesheetMainPage();
                fragmentTransaction.addToBackStack(null);
                if(logintype!= null) {
                    if (logintype.equals("Admin"))
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                }
                else
                    fragmentTransaction.replace(R.id.fragmentcontainer,fragment);
                fragmentTransaction.commit();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });
}


    public int maxtimesheetid(){
        Firebaselinks.timesheetfirebaselink().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                tid = Integer.parseInt(childSnapshot.child("timesheetid").getValue().toString());
                            }
                            tid = tid + 1;
                        }
                        else
                            tid = 1;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }


                });
        return tid;
    }

public void savemethod(){


        if(!txtnoofhrs.getText().toString().isEmpty()) {
            final String id = Firebaselinks.timesheetfirebaselink().push().getKey();


            Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (final DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.child("projname").getValue().toString().equals(txtprojname.getText().toString()) &&
                                    child.child("projdesc").getValue().toString().equals(txtprojdesc.getText().toString())) {
                                if (!txtnoofhrs.getText().toString().isEmpty()) {
                                    Firebaselinks.timesheetfirebaselink().child(id).child("timesheetid").setValue(tid);
                                    Firebaselinks.timesheetfirebaselink().child(id).child("date").setValue(Validations.ddtoyyyywithtime(date));
                                    Firebaselinks.timesheetfirebaselink().child(id).child("projid").setValue(child.child("projid").getValue().toString());
                                    Firebaselinks.timesheetfirebaselink().child(id).child("noofhrs").setValue(txtnoofhrs.getText().toString());
                                    Firebaselinks.timesheetfirebaselink().child(id).child("empid").setValue(empid);
                                    Firebaselinks.timesheetfirebaselink().child(id).child("paytype").setValue(txtpaytype.getSelectedItem().toString());
                                    Firebaselinks.timesheetfirebaselink().child(id).child("desc").setValue(txtdesc.getText().toString());
                                    Firebaselinks.timesheetfirebaselink().child(id).child("status").setValue("Complete");
                                    Firebaselinks.timesheetfirebaselink().child(id).child("confirm").setValue("no");


                                }
                            }
                        }
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        AddTimesheetMainPage fragment = new AddTimesheetMainPage();
                        fragmentTransaction.addToBackStack(null);
                        if(logintype!= null) {
                            if (logintype.equals("Admin"))
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                        }
                        else
                            fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                        fragmentTransaction.commit();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }else
            Validations.Toast(getContext(),"Please fill hours");

}

    public void deletemethod() {



        Firebaselinks.timesheetfirebaselink().orderByChild("timesheetid").equalTo(tid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("empid").getValue().toString().equals(empid)) {
                            String clubkey = child.getKey();
                            Firebaselinks.timesheetfirebaselink().child(clubkey).removeValue();
                        }
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    AddTimesheetMainPage fragment = new AddTimesheetMainPage();
                    fragmentTransaction.addToBackStack(null);
                    if(logintype!= null) {
                        if (logintype.equals("Admin"))
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                    }
                    else
                        fragmentTransaction.replace(R.id.fragmentcontainer,fragment);
                    fragmentTransaction.commit();
                    fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
