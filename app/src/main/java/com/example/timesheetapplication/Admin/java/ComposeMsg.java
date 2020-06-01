package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ComposeMsg extends Fragment {
    EditText txtemp, txttext, txttitle;
    String ids;
    int notifyid;
    View view;
    Button savebtn;

    public String empid, empname;
String emaild;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Send Message";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_compose_msg, container, false);

        txtemp = view.findViewById(R.id.txtemp);
        txttext = view.findViewById(R.id.txttext);
        txttitle = view.findViewById(R.id.txttitle);
        savebtn = view.findViewById(R.id.Savebtn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_clicked();
            }
        });

        notifyid = maxnotifyid();

        txtemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(empname!= null) {
                    Intent intent = new Intent(getContext(), MultiSelectCheckboxSpinner.class);
                    intent.putExtra("empname",empname);
                    intent.putExtra("supervisor","supervisor");
                    startActivityForResult(intent, 1);
                }else
                    startActivityForResult(new Intent(getContext(), MultiSelectCheckboxSpinner.class), 1);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            super.onActivityResult(requestCode, resultCode, data);

            txtemp.setText(data.getStringExtra("result"));
            ids = data.getStringExtra("resultids");
        }

    }

    public void save_clicked() {



        if(!txtemp.getText().toString().isEmpty() && !txttext.getText().toString().isEmpty()) {
            String id = Firebaselinks.notificationsfirebaselink().push().getKey();
            Firebaselinks.notificationsfirebaselink().child(id).child("notifyid").setValue(notifyid);
            Firebaselinks.notificationsfirebaselink().child(id).child("date").setValue(SetDate.Currentdateinyyyymmdd());
            Firebaselinks.notificationsfirebaselink().child(id).child("empid").setValue(ids);
            Firebaselinks.notificationsfirebaselink().child(id).child("sentby").setValue(empid);
            Firebaselinks.notificationsfirebaselink().child(id).child("title").setValue(txttitle.getText().toString());
            Firebaselinks.notificationsfirebaselink().child(id).child("message").setValue(txttext.getText().toString());
            Firebaselinks.notificationsfirebaselink().child(id).child("status").setValue("");


            Validations.Toast(getContext(), "Message sent successfully");
            Validations.ClearDataWithoutHint(txtemp, txttext, txttitle);
        }
        else
            Validations.Toast(getContext(),"Please fill all details");
    }


//
//if(ids.contains(",")) {
//    String[] h = ids.split(",");
//for(int i=0;i<h.length;i++){
//    if(h[i].equals(empid)){
//
//        NotificationManager notif=(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notify=new Notification.Builder
//                (getContext()).setContentTitle(txttitle.getText().toString()).setContentText(txttext.getText().toString()).
//                setSmallIcon(R.drawable.ic_appointment).build();
//
//        notify.flags |= Notification.FLAG_AUTO_CANCEL;
//        notif.notify(0, notify);
//
//    }
//}
//}



    public int maxnotifyid(){
        Firebaselinks.notificationsfirebaselink().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                notifyid = Integer.parseInt(childSnapshot.child("notifyid").getValue().toString());
                            }
                            notifyid = notifyid + 1;
                        }
                        else
                            notifyid = 1;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }


                });
        return notifyid;
    }
}
