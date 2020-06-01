package com.example.timesheetapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class fragmentviewpaytype extends Fragment {
View view;
    RecyclerView recyclerView;
    Button editbtn,deletebtn;
    EditText txtpaytype,txtdesc,txtmultiplier;
    RadioButton yesradiobtn,noradiobtn;
    RadioGroup radioGroup;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



      view = inflater.inflate(R.layout.fragment_fragmentviewpaytype, container, false);


        txtpaytype = view.findViewById(R.id.paytype);
        txtdesc = view.findViewById(R.id.description);
        txtmultiplier= view.findViewById(R.id.multiplier);
        editbtn= view.findViewById(R.id.editbtn);
        deletebtn= view.findViewById(R.id.deletebtn);
        recyclerView = view.findViewById(R.id.recyclerview);
        radioGroup = view.findViewById(R.id.radioDefault);
        yesradiobtn = view.findViewById(R.id.yes);
        noradiobtn = view.findViewById(R.id.no);


        txtpaytype.setEnabled(false);
        Firebaselinks.paytypefirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("default").getValue().toString().equals("Y")){
                            yesradiobtn.setEnabled(false);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            txtdesc.setText(bundle.getString("desc"));
            txtpaytype.setText(bundle.getString("paytype"));
            txtmultiplier.setText(bundle.getString("multiplier"));
          String b = bundle.getString("default");


if(b.equals("N")){
    noradiobtn.setChecked(true);
}
if(b.equals("Y")){
    yesradiobtn.setChecked(true);
}
        }
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebaselinks.paytypefirebaselink().orderByChild("paytype").equalTo(txtpaytype.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                String key = child.getKey();
                                Firebaselinks.paytypefirebaselink().child(key+"/paytype").setValue(txtpaytype.getText().toString());
                                Firebaselinks.paytypefirebaselink().child(key+"/description").setValue(txtdesc.getText().toString());
                                Firebaselinks.paytypefirebaselink().child(key+"/multiplier").setValue(txtmultiplier.getText().toString());

                                if(yesradiobtn.isChecked())
                                Firebaselinks.paytypefirebaselink().child(key+"/default").setValue(yesradiobtn.getText().toString());
                                else
                                    Firebaselinks.paytypefirebaselink().child(key+"/default").setValue(noradiobtn.getText().toString());
                            }
                            Validations.Toast(getContext(),"Updated Successfully");
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            AddPayType fragment = new AddPayType();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
                            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });



        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebaselinks.paytypefirebaselink().orderByChild("paytype").equalTo(txtpaytype.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                String key = child.getKey();
                                Firebaselinks.paytypefirebaselink().child(key).removeValue();
                            }
                            Validations.Toast(getContext(),"Deleted Successfully");
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            AddPayType fragment = new AddPayType();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
                            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });



      return view;
    }
}
