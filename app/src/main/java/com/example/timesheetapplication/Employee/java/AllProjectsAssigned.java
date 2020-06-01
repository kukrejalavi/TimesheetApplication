package com.example.timesheetapplication.Employee.java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.timesheetapplication.Employee.java.AddTimesheet;
import com.example.timesheetapplication.Employee.java.AddTimesheetMainPage;
import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
public class AllProjectsAssigned extends Fragment {
RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    View view;
  public  String empid,type,logintype,date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.activity_all_projects_assigned, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();

DataLoad();

        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                AddTimesheet fragment = new AddTimesheet();
                fragment.empid = empid;
                Bundle bundle = new Bundle();
                bundle.putString("projname", dataAdapter.get(position).getText().toString());
                bundle.putString("projdesc", dataAdapter.get(position).getDesc().toString());
                bundle.putString("date",date);
                bundle.putString("type","AllProjects");
                fragment.setArguments(bundle);


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                if(logintype!= null) {
                    if (logintype.equals("Admin")) {
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragment.logintype = "Admin";
                    }
                }
                else
                    fragmentTransaction.replace(R.id.fragmentcontainer,fragment);
                fragmentTransaction.commit();

            }
        };
        Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);

        
        return view;
    }





    public void DataLoad(){
        dataAdapter = new ArrayList<>();
        final List<String> items = new ArrayList<>();
        Firebaselinks.assignedprojectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("assignedids").getValue().toString().contains(",")) {

                            String h = child.child("assignedids").getValue().toString();

                            String[] f = h.split(",");


                            for (int i = 0; i < f.length; i++) {
                                if (f[i].contains(" ")) {
                                    f[i] = f[i].replace(" ", "");
                                }
                                if (f[i].equals(empid)) {
                                    items.add(child.child("projid").getValue().toString());
                                }


                            }
                        }else{
                            if (child.child("assignedids").getValue().toString().equals(empid)) {
                                items.add(child.child("projid").getValue().toString());
                            }
                        }

                    }

                    Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot1) {
                                 if (dataSnapshot1.getChildrenCount() > 0) {
                                     for (DataSnapshot childtimesheet : dataSnapshot1.getChildren()) {
                                         if (items.contains(childtimesheet.child("projid").getValue().toString())) {
                                             isi = new DataAdapter(childtimesheet.child("projname").getValue().toString(),childtimesheet.child("projdesc").getValue().toString());
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
            View v = LayoutInflater.from(context).inflate(R.layout.checkboxandtextviewrowdata, parent, false);
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
            holder.desc.setText(data.get(position).getDesc());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txt,desc;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                desc = itemView.findViewById(R.id.desc);
               // checkBox = itemView.findViewById(R.id.checkbox);
             //   txtsendtxt = itemView.findViewById(R.id.senddesctoemp);

              //  checkBox.setVisibility(View.GONE);
             //   txtsendtxt.setVisibility(View.GONE);
                //desc.setGravity(Gravity.CENTER_HORIZONTAL);
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

        String text, desc;

        public String getText() {
            return text;
        }


        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setText(String text) {
            this.text = text;
        }

        public DataAdapter(String text, String desc) {
            this.text = text;
            this.desc = desc;

        }
    }
}
