package com.example.timesheetapplication.Admin.java;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.opencensus.stats.AggregationData;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentWorking extends Fragment {
    RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    AppCompatEditText txtsearch;
View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
view = inflater.inflate(R.layout.fragment_current_working, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();
        txtsearch = view.findViewById(R.id.searchtxt);

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

        DataLoad();
        Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);

        return view;
    }
public void DataLoad(){
    dataAdapter = new ArrayList<>();


    Firebaselinks.assignedprojectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0){
                final String[] name = new String[1];
                for(final DataSnapshot  child : dataSnapshot.getChildren()) {

                        Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(child.child("projid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount() > 0){
                                    for(DataSnapshot namechild: dataSnapshot.getChildren()){
                                        name[0] =     namechild.child("projname").getValue().toString();
                                    }

                                    if (child.child("assignedids").getValue().toString().contains(",")) {
                                        final String h[] = child.child("assignedids").getValue().toString().split(",");
                                        for(int i=0;i<h.length;i++) {

                                            Firebaselinks.myaccountfirebaselink().orderByChild("id").equalTo(h[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.getChildrenCount()>0){
                                                        for(DataSnapshot child:dataSnapshot.getChildren()) {
                                                            isi = new DataAdapter(Validations.yyyytoddwithouttime(child.child("date").getValue().toString()), name[0], child.child("name").getValue().toString());
                                                            dataAdapter.add(isi);
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
                                    } else {

                                        Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getChildrenCount()>0){
                                                    for(DataSnapshot innerchild:dataSnapshot.getChildren()) {
                                                        if (innerchild.child("id").equals(child.child("assignedids").getValue().toString())) {
                                                            isi = new DataAdapter(Validations.yyyytoddwithouttime(child.child("date").getValue().toString()), name[0], child.child("name").getValue().toString());
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
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

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
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;

        public CustomAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.threetextviewsrowdata, parent, false);
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
                        if (!item.getTxtprojname().toLowerCase().equals(null)) {
                            if (item.getTxtempname().toLowerCase().contains(filterPattern)) {
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
            holder.txtdate.setText(data.get(position).getTxtdate());
            holder.txtprojname.setText(data.get(position).getTxtprojname());
            holder.txtempname.setText(data.get(position).getTxtempname());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txtdate,txtprojname,txtempname;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txtdate = itemView.findViewById(R.id.first);
                txtprojname = itemView.findViewById(R.id.second);
                txtempname = itemView.findViewById(R.id.third);
                this.mListener=mListener;
                itemView.setOnClickListener(this);



            }

            @Override
            public void onClick(View v) {
                if(mListener!= null)
                    mListener.onClick(v, getAdapterPosition());
            }
        }

    }

    public  class DataAdapter {

        String txtdate,txtprojname,txtempname;

        public DataAdapter(String txtdate,String txtprojname,String txtempname) {
            this.txtdate =txtdate;
            this.txtprojname=txtprojname;
            this.txtempname =txtempname;
        }

        public String getTxtdate() {
            return txtdate;
        }

        public void setTxtdate(String txtdate) {
            this.txtdate = txtdate;
        }

        public String getTxtprojname() {
            return txtprojname;
        }

        public void setTxtprojname(String txtprojname) {
            this.txtprojname = txtprojname;
        }

        public String getTxtempname() {
            return txtempname;
        }

        public void setTxtempname(String txtempname) {
            this.txtempname = txtempname;
        }
    }
}
