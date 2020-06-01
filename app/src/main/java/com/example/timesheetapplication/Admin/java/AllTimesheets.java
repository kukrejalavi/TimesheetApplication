package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AllTimesheets extends Fragment {
    RecyclerViewClickListener listener;
    DataAdapter isi;
    RecyclerView recyclerView;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    int position;
    ImageButton leftarrowbtn, rightarrowbtn;
    View view;
    public String empid,empname;
    CheckBox chkdate,chkname;
    AppCompatEditText txtsearch;
    EditText txtdate;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Timesheets";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.activity_all_timesheets, container, false);
        dataAdapter = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        chkdate = view.findViewById(R.id.chkdatefilter);
        chkname = view.findViewById(R.id.chknamefilter);
        txtdate = view.findViewById(R.id.date);
        txtsearch = view.findViewById(R.id.searchtxt);
        leftarrowbtn = view.findViewById(R.id.leftarrow);
        rightarrowbtn = view.findViewById(R.id.rightarrow);

        leftarrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftrightarrowclick("Left");
            }
        });

        rightarrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftrightarrowclick("Right");
            }
        });
        chkdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Loaddata("");
            }
        });

        chkname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loaddata("");
            }
        });


        txtdate.setText(SetDate.withoutonclickDate(txtdate,getContext()));

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter!= null) {
                    if (chkname.isChecked())
                        adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
Loaddata("");


return  view;
    }

    public void leftrightarrowclick(String arrowclick){
int s = dataAdapter.size();
        if(arrowclick.equals("Left")){
            dataAdapter.clear();

            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
            recyclerView.setAdapter(adapter);
            txtdate.setText(SetDate.dateminusone(txtdate));
            leftarrowbtn.setEnabled(false);
            Loaddata("Left");
        }

        if(arrowclick.equals("Right")) {


            if (!SetDate.Currentdate().equals(txtdate.getText().toString())) {

                dataAdapter.clear();


                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                recyclerView.setAdapter(adapter);
                txtdate.setText(SetDate.dateplusone(txtdate));
                rightarrowbtn.setEnabled(false);
                Loaddata("Right");
            }
        }
        }

    public void Loaddata(final String type){
        dataAdapter = new ArrayList<>();

        if(dataAdapter.size() > 0) {
            dataAdapter.clear();

            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
            recyclerView.setAdapter(adapter);
        }

        Firebaselinks.timesheetfirebaselink().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0) {
                            if(dataAdapter.size() > 0) {
                                dataAdapter.clear();

                                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                recyclerView.setAdapter(adapter);
                            }

                            for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                                Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getChildrenCount() > 0){


                                            for(DataSnapshot getname : dataSnapshot.getChildren()) {
                                                if (getname.child("id").getValue().toString().equals(childSnapshot.child("empid").getValue().toString())) {
                                                    if(childSnapshot.child("status").getValue().toString().equals("Complete") && childSnapshot.child("confirm").getValue().toString().equals("yes")){
                                                        if(chkdate.isChecked()){
                                                            if(Validations.newyyyytoddwithouttime(childSnapshot.child("date").getValue().toString()).equals(Validations.slashtodash(txtdate.getText().toString()))){
                                                                isi = new DataAdapter(childSnapshot.child("empid").getValue().toString(), getname.child("name").getValue().toString(),
                                                                        childSnapshot.child("date").getValue().toString(), childSnapshot.child("noofhrs").getValue().toString());
                                                                dataAdapter.add(isi);
                                                            }
                                                        }
                                                        else if(chkname.isChecked()){
                                                            if(getname.child("name").getValue().toString().toLowerCase().contains(txtsearch.getText().toString().toLowerCase())) {
                                                                isi = new DataAdapter(childSnapshot.child("empid").getValue().toString(), getname.child("name").getValue().toString(),
                                                                        childSnapshot.child("date").getValue().toString(), childSnapshot.child("noofhrs").getValue().toString());
                                                                dataAdapter.add(isi);
                                                            }
                                                        }
                                                        else{
                                                            isi = new DataAdapter(childSnapshot.child("empid").getValue().toString(), getname.child("name").getValue().toString(),
                                                                    childSnapshot.child("date").getValue().toString(), childSnapshot.child("noofhrs").getValue().toString());
                                                            dataAdapter.add(isi);
                                                        }
                                                }
                                            }
                                            }





                                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                            recyclerView.setAdapter(adapter);
                                            if(type.equals("Left"))
                                                leftarrowbtn.setEnabled(true);


                                            if(type.equals("Right"))
                                                rightarrowbtn.setEnabled(true);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });


                            }





//                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
//                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
//                            recyclerView.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }


                });

    }
    public void approve(final int position){

        //db.execSQL("delete from Orders where orderid='" + dataAdapter.get(position).getOid() + "'");

        Firebaselinks.timesheetfirebaselink().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (dataAdapter.size() > 0) {
                                if (child.child("date").getValue().toString().equals(dataAdapter.get(position).getDate())) {
                                    String key = child.getRef().getKey();
                                    Firebaselinks.timesheetfirebaselink().child(key + "/status").setValue("Approved");
                                }
                            }
                        }

                        if (dataAdapter.size() > 0) {
                            dataAdapter.clear();
                            Loaddata("");
                        }


                    }


                    else
                    {
                        if (dataAdapter.size() > 0) {
                            dataAdapter.clear();
                            Loaddata("");
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void reject(final int position){
RejectReason Rejectreason = new RejectReason();
        Intent intent = new Intent(getContext(),Rejectreason.getClass());
        Rejectreason.position = position;
        this.position = position;
        startActivityForResult(intent,1);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        System.exit(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==1){
            super.onActivityResult(requestCode, resultCode, data);
            final String reason = data.getStringExtra("result");
           // final int position =Integer.parseInt(data.getStringExtra("position"));

            Firebaselinks.timesheetfirebaselink().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if (dataAdapter.size() > 0) {
                                    if (child.child("date").getValue().toString().equals(dataAdapter.get(position).getDate())) {
                                        String key = child.getRef().getKey();
                                        Firebaselinks.timesheetfirebaselink().child(key + "/status").setValue("Rejected");
                                        Firebaselinks.timesheetfirebaselink().child(key + "/reason").setValue(reason);
                                    }
                                }
                            }

                            if (dataAdapter.size() > 0) {
                                dataAdapter.clear();
                                Loaddata("");
                            }


                        } else {
                            if (dataAdapter.size() > 0) {
                                dataAdapter.clear();
                                Loaddata("");
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.timesheetapprovalrowdata, parent, false);
            return new MyHolder(v, listener);
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
                        if (!item.getEmpname().toLowerCase().equals(null)) {
                            if (item.getEmpname().toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
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
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.txtempid.setText(data.get(position).getEmpid());
            holder.txtempname.setText(data.get(position).getEmpname());
            holder.txtdate.setText(data.get(position).getDate());
            holder.txtnoofhrs.setText(data.get(position).getNoofhrs());


        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txtempid,txtempname,txtdate,txtnoofhrs;
            ImageView btnapprove,btnreject;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txtempname = itemView.findViewById(R.id.name);
                txtempid = itemView.findViewById(R.id.empid);
                txtdate = itemView.findViewById(R.id.date);
                txtnoofhrs = itemView.findViewById(R.id.noofhrs);
                btnapprove = itemView.findViewById(R.id.approve);
                btnreject = itemView.findViewById(R.id.reject);
                this.mListener=mListener;
                itemView.setOnClickListener(this);

                btnapprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     approve(getAdapterPosition());
                    }
                });



                btnreject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       reject(getAdapterPosition());
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

        String empid,empname,date,noofhrs;

        public String getEmpname() {
            return empname;
        }

        public void setEmpname(String empname) {
            this.empname = empname;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNoofhrs() {
            return noofhrs;
        }

        public void setNoofhrs(String noofhrs) {
            this.noofhrs = noofhrs;
        }

        public String getEmpid() {
            return empid;
        }

        public void setEmpid(String empid) {
            this.empid = empid;
        }

        public DataAdapter(String empid, String empname, String date, String noofhrs) {
            this.empid =empid;
            this.empname=empname;
            this.date = date;
            this.noofhrs = noofhrs;
        }


    }
}
