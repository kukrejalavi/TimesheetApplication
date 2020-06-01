package com.example.timesheetapplication;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timesheetapplication.Admin.java.SelectEmps;
import com.example.timesheetapplication.Admin.java.SelectProject;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignAdmin extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    Spinner txtadmin;
    EditText txtpeople;
    Button assignbtn;
    View view;
    final List<String> adminlist = new ArrayList<>();
    final List<String> peoplelist = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Assign Admin";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_assign_admin, container, false);


        dataAdapter = new ArrayList<>();
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView = view.findViewById(R.id.recyclerview);
        txtadmin = view.findViewById(R.id.txtadmin);
        txtpeople= view.findViewById(R.id.txtpeople);
        assignbtn = view.findViewById(R.id.Assignbtn);


        adminlist.add("Select Admin");


        txtpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), SelectEmps.class);
                    if (!txtadmin.getSelectedItem().toString().isEmpty() || txtadmin.getSelectedItem().toString() != null) {

                        intent.putExtra("admin", txtadmin.getSelectedItem().toString());

                        startActivityForResult(intent, 1);
                    }


                }catch (Exception e){}
            }
        });


        txtadmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemchanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        assignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtadmin.getSelectedItem().toString().isEmpty() && !txtpeople.getText().toString().isEmpty()) {
                    if (!txtadmin.getSelectedItem().toString().equals("Select Admin") && !txtpeople.getText().toString().equals("Select Employee/Supervisor")) {
                        Firebaselinks.adminfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (child.child("admin").getValue().toString().equals(txtadmin.getSelectedItem().toString())) {
                                            String key = child.getKey();
                                            Firebaselinks.adminfirebaselink().child(key + "/people").setValue(child.child("people").getValue().toString() + "," + txtpeople.getText().toString());

                                            Validations.Toast(getContext(), "Assigned Successfully");
txtpeople.setText("");
                                            //  txtcostcenter.setSelection(0);
                                            DataLoad();
                                            return;
                                        }


                                    }
                                    String id = Firebaselinks.adminfirebaselink().push().getKey();
                                    Firebaselinks.adminfirebaselink().child(id).child("admin").setValue(txtadmin.getSelectedItem().toString());
                                    Firebaselinks.adminfirebaselink().child(id).child("people").setValue(txtpeople.getText().toString());

                                    Validations.Toast(getContext(), "Assigned Successfully");
                                    txtpeople.setText("");
                                    DataLoad();
                                } else {
                                    String id = Firebaselinks.adminfirebaselink().push().getKey();
                                    Firebaselinks.adminfirebaselink().child(id).child("admin").setValue(txtadmin.getSelectedItem().toString());
                                    Firebaselinks.adminfirebaselink().child(id).child("people").setValue(txtpeople.getText().toString());

                                    Validations.Toast(getContext(), "Assigned Successfully");
                                    txtpeople.setText("");
                                    DataLoad();
                                    //      txtcostcenter.setSelection(0);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }
            }
        });



        Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    try {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.child("profile").getValue().toString().equals("Admin") && child.child("status").getValue().toString().equals("Active")) {
                                adminlist.add(child.child("name").getValue().toString());
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, adminlist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        txtadmin.setAdapter(adapter);
                    }catch(Exception e){}
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK && requestCode==1) {
            super.onActivityResult(requestCode, resultCode, data);

            if(data.getStringExtra("result")!= null)
                txtpeople.setText(data.getStringExtra("result"));


        }
    }

    public void itemchanged(){
        if(dataAdapter.size() > 0)
            dataAdapter.clear();
        Firebaselinks.adminfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child  :dataSnapshot.getChildren()){
                        if(child.child("admin").getValue().toString().equals(txtadmin.getSelectedItem().toString())){
                            if(child.child("people").getValue().toString().contains(",")) {
                                String h[]=child.child("people").getValue().toString().split(",");
                                for(int i=0;i<h.length;i++) {
                                    isi = new DataAdapter(h[i]);
                                    dataAdapter.add(isi);
                                }
                            }
                            else{
                                isi = new DataAdapter(child.child("people").getValue().toString());
                                dataAdapter.add(isi);
                            }
                        }
                    }

                    Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                    adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void DataLoad(){
        if(dataAdapter.size() > 0)
            dataAdapter.clear();


        Firebaselinks.adminfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child  :dataSnapshot.getChildren()){
                        if(child.child("admin").getValue().toString().equals(txtadmin.getSelectedItem().toString())){
                            if(child.child("people").getValue().toString().contains(",")) {
                                String h[]=child.child("people").getValue().toString().split(",");
                                for(int i=0;i<h.length;i++) {
                                    isi = new DataAdapter(h[i]);
                                    dataAdapter.add(isi);
                                }
                            }
                            else{
                                isi = new DataAdapter(child.child("people").getValue().toString());
                                dataAdapter.add(isi);
                            }
                        }
                    }


                    Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                    adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





    }
    public void delete(final int pos){

        final List<String> projlist = new ArrayList<>();
        Firebaselinks.adminfirebaselink().orderByChild("admin").equalTo(txtadmin.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("people").getValue().toString().contains(",")){

                            if(child.child("people").getValue().toString().contains(dataAdapter.get(pos).getText())){
                                String[]h= child.child("people").getValue().toString().split(",");
                                for(int i=0;i<h.length;i++) {
                                    projlist.add(h[i]);
                                }

                                for(int i=0;i<projlist.size();i++) {
                                    if (projlist.get(i).equals(dataAdapter.get(pos).getText())) {
                                        projlist.remove(i);
                                    }
                                }
                                String key = child.getKey();
                                Firebaselinks.adminfirebaselink().child(key + "/people").setValue(projlist.toString().replace("[", "").replace("]", ""));
                                dataAdapter.remove(pos);
                                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                recyclerView.setAdapter(adapter);
                            }

                        }
                        else{
                            if(child.child("people").getValue().toString().equals(dataAdapter.get(pos).getText())){
                                String key = child.getKey();
                                Firebaselinks.adminfirebaselink().child(key + "/people").setValue("");
                                dataAdapter.remove(pos);
                                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                recyclerView.setAdapter(adapter);
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


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;

        public CustomAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.selectemprowdata, parent, false);
            return new CustomAdapter.MyHolder(v, listener);
        }

        public CustomAdapter(Context context, List<DataAdapter> data, RecyclerViewClickListener listener) {
            this.context = context;
            this.data = data;
            data1 = new ArrayList<>(data);
            this.listener = listener;
        }
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<DataAdapter> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(data1);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (DataAdapter item : data1) {
                        if (!item.getText().toLowerCase().equals(null)) {
                            if (item.getText().toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
                    // int h = items.size();
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                data.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };




        public Filter getFilter() {
            return exampleFilter;
        }
        public void onBindViewHolder(CustomAdapter.MyHolder holder, int position) {
            holder.txt.setText(data.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txt,id,role;
            ImageButton clearbtn;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                clearbtn = itemView.findViewById(R.id.clearbtn);
                id = itemView.findViewById(R.id.id);
                role = itemView.findViewById(R.id.role);
                role.setVisibility(View.GONE);
                this.mListener=mListener;
                itemView.setOnClickListener(this);


                clearbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(getAdapterPosition());
                    }
                });
            }

            @Override
            public void onClick(View v) {
                if(mListener!= null)
                    mListener.onClick(v, getAdapterPosition());
            }
        }

    }

    public  class DataAdapter {

        String text,id,role;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getText() {
            return text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setText(String text) {
            this.text = text;
        }



        public DataAdapter(String text) {
            this.text =text;
        }


        public DataAdapter(String text,String id,String role) {
            this.text =text;
            this.id=id;
            this.role= role;
        }

    }
}
