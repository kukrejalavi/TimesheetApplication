package com.example.timesheetapplication.Admin.java;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timesheetapplication.AddPayType;
import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class editordeletecatgory extends Fragment {
    View view;

    ImageButton addcategorybtn;
    Spinner spinnercategory,spinnerproject;
    EditText txtrates;
    TextView txtenddate;
    Button editbtn,deletebtn;
    List<String> projectlist = new ArrayList<>();
    List<String> categorylist = new ArrayList<>();



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Modify Category";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_editordeletecatgory, container, false);

        spinnercategory = view.findViewById(R.id.spinnercategory);
        spinnerproject = view.findViewById(R.id.projectspinner);
        txtenddate = view.findViewById(R.id.enddate);
        txtrates = view.findViewById(R.id.rates);
        addcategorybtn = view.findViewById(R.id.addcategory);
        editbtn = view.findViewById(R.id.editbtn);
        deletebtn = view.findViewById(R.id.deletebtn);

spinnerproject.setEnabled(false);
spinnercategory.setEnabled(false);


txtenddate.setText(SetDate.Date(txtenddate,getContext()));




        Bundle bundle = this.getArguments();

        if (bundle != null) {
            txtenddate.setText(bundle.getString("enddate"));
            txtrates.setText(bundle.getString("rate"));
            projectlist.add(bundle.getString("project"));
            categorylist.add(bundle.getString("category"));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, projectlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerproject.setAdapter(adapter);

            adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, categorylist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnercategory.setAdapter(adapter);


            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebaselinks.projectcategoryfirebaselink().orderByChild("project").equalTo(spinnerproject.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() > 0) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.child("category").getValue().toString().equals(spinnercategory.getSelectedItem().toString())){
                                        String key = child.getKey();


                                    String nextyear = SetDate.dateplusoneyear();

                                    String selecteddate = Validations.ddtoyyyywithouttime(Validations.slashtodash(txtenddate.getText().toString()));
                                    String todaydate = SetDate.Currentdateinyyyymmddwithouttime();

                                    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date nextyeardate = null;
                                    try {
                                        nextyeardate = sdformat.parse(nextyear);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date d1 = null;
                                    try {
                                        d1 = sdformat.parse(selecteddate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date d2 = null;
                                    try {
                                        d2 = sdformat.parse(todaydate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((d1.compareTo(d2) > 0) && d1.compareTo(nextyeardate) > 0) {
                                        if (!txtenddate.getText().toString().equals(SetDate.Currentdate())) {

                                            if (!txtenddate.getText().toString().isEmpty() && !txtrates.getText().toString().isEmpty() && !spinnercategory.getSelectedItem().toString().isEmpty() && !spinnerproject.getSelectedItem().toString().isEmpty()) {
                                                Firebaselinks.projectcategoryfirebaselink().child(key + "/project").setValue(spinnerproject.getSelectedItem().toString());
                                                Firebaselinks.projectcategoryfirebaselink().child(key + "/category").setValue(spinnercategory.getSelectedItem().toString());
                                                Firebaselinks.projectcategoryfirebaselink().child(key + "/enddate").setValue(Validations.slashtodash(Validations.ddtoyyyywithouttime(Validations.slashtodash(txtenddate.getText().toString()))));

                                                Firebaselinks.projectcategoryfirebaselink().child(key + "/rates").setValue(txtrates.getText().toString());
                                            }
                                            Validations.Toast(getContext(), "Updated Successfully");
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            AddCategory fragment = new AddCategory();
                                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.replace(R.id.content_frame, fragment);
                                            fragmentTransaction.commit();
                                            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        }
                                    }
                                }
                            }
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
                    Firebaselinks.projectcategoryfirebaselink().orderByChild("project").equalTo(spinnerproject.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() > 0){
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    String key = child.getKey();
                                    Firebaselinks.projectcategoryfirebaselink().child(key).removeValue();
                                }
                                Validations.Toast(getContext(),"Deleted Successfully");
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                AddCategory fragment = new AddCategory();
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
            Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if(!projectlist.contains(child.child("projname").getValue().toString()))
                            projectlist.add(child.child("projname").getValue().toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, projectlist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerproject.setAdapter(adapter);

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            Firebaselinks.projectcategoryfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if(!categorylist.contains(child.child("category").getValue().toString()))
                            categorylist.add(child.child("category").getValue().toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, categorylist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnercategory.setAdapter(adapter);

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        }
        return view;
    }
}
