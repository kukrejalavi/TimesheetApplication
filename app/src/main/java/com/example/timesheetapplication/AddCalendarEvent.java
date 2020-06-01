package com.example.timesheetapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCalendarEvent extends Fragment {
Button savebtn,editbtn,deletebtn;
TextView txtdate;
EditText txtnote;
View view;
int eventid;
     Bundle bundle;
    public String empid,empname,logintype;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_add_calendar_event, container, false);



        txtdate = view.findViewById(R.id.date);
        txtnote = view.findViewById(R.id.note);
        savebtn = view.findViewById(R.id.savebtn);
        editbtn = view.findViewById(R.id.editbtn);
        deletebtn = view.findViewById(R.id.deletebtn);

maxeventid();
       bundle = this.getArguments();

        txtdate.setText(bundle.getString("date"));

        if(bundle.getString("typeeditdelete") !=null){
            editbtn.setVisibility(View.VISIBLE);
            deletebtn.setVisibility(View.VISIBLE);
            savebtn.setVisibility(View.GONE);
eventid =Integer.parseInt(bundle.getString("eventid"));
            txtnote.setText(bundle.getString("note"));
        }
        if(bundle.getString("typeadd") !=null){
            editbtn.setVisibility(View.GONE);
            deletebtn.setVisibility(View.GONE);
            savebtn.setVisibility(View.VISIBLE);
        }

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myDate = txtdate.getText().toString();

                String id = Firebaselinks.calendarfirebaselink().push().getKey();
                Firebaselinks.calendarfirebaselink().child(id).child("eventid").setValue(maxeventid());
                Firebaselinks.calendarfirebaselink().child(id).child("empid").setValue(empid);
                Firebaselinks.calendarfirebaselink().child(id).child("empname").setValue(empname);
                Firebaselinks.calendarfirebaselink().child(id).child("date").setValue(Validations.currentDate().split(" ")[0]);
                Firebaselinks.calendarfirebaselink().child(id).child("eventdate").setValue(Validations.ddtoyyyywithouttime(myDate));
                Firebaselinks.calendarfirebaselink().child(id).child("note").setValue(txtnote.getText().toString());


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                CalendarView fragment = new CalendarView();
                fragmentTransaction.addToBackStack(null);
                if(logintype!= null) {
                    if (logintype.equals("Admin")) {
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragment.logintype="Admin";
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    }
                }
                else {
                    fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                    fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragment).commit();
                }
                fragmentTransaction.commit();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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




return view;
    }


    public void deletemethod(){

        Firebaselinks.calendarfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("eventid").getValue().toString().equals(bundle.getString("eventid"))) {
                            String clubkey = child.getKey();
                            Firebaselinks.calendarfirebaselink().child(clubkey).removeValue();
                        }

                    }
                    Validations.Toast(getContext(),"Deleted Successfully");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    CalendarView fragment = new CalendarView();
                    fragmentTransaction.addToBackStack(null);
                    if(logintype!= null) {
                        if (logintype.equals("Admin")) {
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragment.logintype="Admin";
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        }
                    }
                    else {
                        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                        fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragment).commit();
                    }
                    fragmentTransaction.commit();
                    fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    public void editmethod(){
        Firebaselinks.calendarfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("eventid").getValue().toString().equals(bundle.getString("eventid"))) {
                            String clubkey = child.getKey();
                            Firebaselinks.calendarfirebaselink().child(clubkey + "/note").setValue(txtnote.getText().toString());
                        }

                    }
                    Validations.Toast(getContext(),"Updated Successfully");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    CalendarView fragment = new CalendarView();
                    fragmentTransaction.addToBackStack(null);
                    if(logintype!= null) {
                        if (logintype.equals("Admin")) {
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragment.logintype="Admin";
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        }
                    }
                    else {
                        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                        fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragment).commit();
                    }
                    fragmentTransaction.commit();
                    fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public int maxeventid(){
        Firebaselinks.calendarfirebaselink().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                eventid = Integer.parseInt(childSnapshot.child("eventid").getValue().toString());
                            }
                            eventid = eventid + 1;
                        }
                        else
                            eventid = 1;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }


                });
        return eventid;
    }

}
