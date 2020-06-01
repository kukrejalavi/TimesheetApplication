package com.example.timesheetapplication.Admin.java;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EmployeeList extends Fragment {
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    View view;
    public String type;
    AppCompatEditText txtsearch;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Review Employee";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =     inflater.inflate(R.layout.fragment_empandsv_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        txtsearch = view.findViewById(R.id.searchtxt);
        dataAdapter = new ArrayList<>();
        if(type.equals("Employee"))
            DataLoad("Employee");


        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(dataAdapter.get(position).getChk()==true) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    Profile fragment = new Profile();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", dataAdapter.get(position).getText().toString());
                    bundle.putString("id", dataAdapter.get(position).getId().toString());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }else
                    Validations.Toast(getContext(),"Employee is Inactive");
            }
        };

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter!= null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);
        return view;
    }


    public void DataLoad(final String type){
        if(type.equals("Employee")){
            Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        for(DataSnapshot childsnapshot : dataSnapshot.getChildren()){
if(childsnapshot.child("status").getValue().toString().equals("Active")) {
    isi = new DataAdapter(childsnapshot.child("name").getValue().toString(), childsnapshot.child("id").getValue().toString(), childsnapshot.child("profile").getValue().toString(),true);
    dataAdapter.add(isi);
}
else{
    isi = new DataAdapter(childsnapshot.child("name").getValue().toString(), childsnapshot.child("id").getValue().toString(), childsnapshot.child("profile").getValue().toString(),false);
    dataAdapter.add(isi);
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




    }


public void switchmethod(int pos){


        final String role = dataAdapter.get(pos).getRole();
        final String name = dataAdapter.get(pos).getText();
        final String id = dataAdapter.get(pos).getId();


    Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0){
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.child("id").getValue().toString().equals(id)) {
                        if (child.child("name").getValue().toString().equals(name) && child.child("profile").getValue().toString().equals(role)) {
                            String key = child.getKey();
                            Firebaselinks.myaccountfirebaselink().child(key + "/status").setValue("Active");
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


    public void switchmethoduncheck(int pos){


        final String role = dataAdapter.get(pos).getRole();
        final String name = dataAdapter.get(pos).getText();
      final   String id = dataAdapter.get(pos).getId();


        Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("id").getValue().toString().equals(id)) {
                            if (child.child("name").getValue().toString().equals(name) && child.child("profile").getValue().toString().equals(role)) {
                                String key = child.getKey();
                                Firebaselinks.myaccountfirebaselink().child(key + "/status").setValue("Inactive");
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
            View v = LayoutInflater.from(context).inflate(R.layout.togglebuttonrowdata, parent, false);
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
                            if (item.getText().toLowerCase().contains(filterPattern) || item.getRole().toLowerCase().contains(filterPattern)) {
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
            holder.id.setText(data.get(position).getId());
            holder.role.setText(data.get(position).getRole());
            holder.switchCompat.setChecked(data.get(position).getChk());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageButton clearbtn;
            SwitchCompat switchCompat;
            TextView txt,id,role;
            RecyclerViewClickListener mListener;
            public MyHolder(final View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                id = itemView.findViewById(R.id.id);
                role = itemView.findViewById(R.id.role);
                switchCompat = itemView.findViewById(R.id.togglebtn);
                this.mListener=mListener;
              //  clearbtn = itemView.findViewById(R.id.clearbtn);
               // clearbtn.setVisibility(View.GONE);
                itemView.setOnClickListener(this);



                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                        Snackbar.make(buttonView, "Switch state checked "+isChecked, Snackbar.LENGTH_LONG)
//                                .setAction("ACTION",null).show();

                        if(isChecked) {
                            switchmethod(getAdapterPosition());
switchCompat.setChecked(true);
                            itemView.setEnabled(true);
                        }
                        else {

                            switchmethoduncheck(getAdapterPosition());
                            switchCompat.setChecked(false);
                            itemView.setEnabled(false);
                        }
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
Boolean chk;
        public String getRole() {
            return role;
        }

        public Boolean getChk() {
            return chk;
        }

        public void setChk(Boolean chk) {
            this.chk = chk;
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


        public DataAdapter(String text,String id,String role) {
            this.text =text;
            this.id=id;
            this.role= role;
        }


        public DataAdapter(String text,String id,String role,Boolean chk) {
            this.text =text;
            this.id=id;
            this.role= role;
            this.chk= chk;
        }

    }
}
