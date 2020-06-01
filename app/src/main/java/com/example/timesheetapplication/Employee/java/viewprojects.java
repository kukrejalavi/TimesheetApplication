package com.example.timesheetapplication.Employee.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class viewprojects extends Fragment {
RecyclerView recyclerView;
Button Confirmbtn;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    public   String empid,empname;
    View view;
    ImageButton deletebtn;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "View Timesheet";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.activity_viewprojects, container, false);

        recyclerView= view.findViewById(R.id.recyclerview);
        deletebtn= view.findViewById(R.id.Deletebtn);
        Confirmbtn= view.findViewById(R.id.Confirmbtn);



       //   checkforconfirmbtn();

     //   Confirmbtn.setVisibility(View.VISIBLE);
Confirmbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        for(int i= 0;i<recyclerView.getChildCount();i++) {
            if (recyclerView.findViewHolderForLayoutPosition(i) instanceof CustomAdapter.MyHolder) {

                CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(i);




                    final String tid = holder.txttid.getText().toString();

                    Firebaselinks.timesheetfirebaselink().orderByChild("empid").equalTo(empid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() >0 ){
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    if(tid.equals(child.child("timesheetid").getValue().toString()))
                                    {
                                        String key = child.getRef().getKey();
                                        Firebaselinks.timesheetfirebaselink().child(key + "/confirm").setValue("yes");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });



dataAdapter.clear();
Confirmbtn.setVisibility(View.GONE);

                 //   DataLoad();



                }
            }

    }
});
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> tids = new ArrayList<>();
                for(int i= 0;i<recyclerView.getChildCount();i++) {
                    if (recyclerView.findViewHolderForLayoutPosition(i) instanceof CustomAdapter.MyHolder) {

                        CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(i);

                        if (holder.checkbox.isChecked()) {


                             final String tid =  holder.txttid.getText().toString();


                                        Firebaselinks.timesheetfirebaselink().orderByChild("empid").equalTo(empid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getChildrenCount() >0 ){
                                                    for(DataSnapshot child : dataSnapshot.getChildren()){
                                                        if(tid.equals(child.child("timesheetid").getValue().toString()))
                                                        {
                                                            String key = child.getRef().getKey();
                                                           Firebaselinks.timesheetfirebaselink().child(key).removeValue();
                                                        }
                                                    }
                                                    DataLoad();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });






                                        deletebtn.setVisibility(View.GONE);

                        }
                    }
                }

            }
        });

        dataAdapter = new ArrayList<>();
      DataLoad();
        return view;
    }

    public void DataLoad(){
        if(dataAdapter.size() > 0)
            dataAdapter.clear();
        Firebaselinks.timesheetfirebaselink().orderByChild("empid").equalTo(empid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(final DataSnapshot child : dataSnapshot.getChildren()){
                        Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(child.child("projid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                try {
                                    if (dataSnapshot1.getChildrenCount() > 0) {
                                        for (DataSnapshot childtimesheet : dataSnapshot1.getChildren()) {
                                            if (child.child("confirm").getValue().toString().equals("no")) {
                                                isi = new DataAdapter(child.child("timesheetid").getValue().toString(), Validations.dashtoslash(Validations.newyyyytoddwithouttime(child.child("date").getValue().toString())), childtimesheet.child("projname").getValue().toString(), child.child("desc").getValue().toString(), child.child("noofhrs").getValue().toString());
                                                dataAdapter.add(isi);
                                            }
                                        }
                                        Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }catch(Exception e){}
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkforconfirmbtn(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int iYear = year; //Substitue for current year
        Calendar cal = Calendar.getInstance();
        String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month =monthName[cal.get(Calendar.MONTH)];
        int iDay = Integer.parseInt(SetDate.Currentdateonlydd());
        int monthnum =cal.get(Calendar.MONTH) + 1;
       Calendar mycal = new GregorianCalendar(iYear, monthnum, iDay);
        int daysInMonth = mycal.getActualMaximum(monthnum); // 28

//Use this if you want 15th to be mid date for a month of 31 days)
     int midDay= daysInMonth/2 ;

     String day = String.valueOf(midDay);
        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        }


        if(SetDate.Currentdateonlydd().equals(daysInMonth+"") || SetDate.Currentdateonlydd().equals(day)){
Confirmbtn.setVisibility(View.VISIBLE);
        }







//Use this if you want 16th to be mid date for a month of 31 days)
        // midDay = (int)Math.ceil(((double)daysInMonth)/2);

        //  mycal = new GregorianCalendar(iYear, mo, midDay);
    }




    public void checkbox(){
        for(int i= 0;i<recyclerView.getChildCount();i++) {
            if (recyclerView.findViewHolderForLayoutPosition(i) instanceof CustomAdapter.MyHolder) {

                CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(i);

                if(holder.checkbox.isChecked()){

                    deletebtn.setVisibility(View.VISIBLE);

                }




            }
        }
    }


    public void checkboxunchecked(){
        for(int i= 0;i<recyclerView.getChildCount();i++) {
            if (recyclerView.findViewHolderForLayoutPosition(i) instanceof CustomAdapter.MyHolder) {

                CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(i);

                if(!holder.checkbox.isChecked()){

                    deletebtn.setVisibility(View.GONE);

                }




            }
        }
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.confirmtimesheetrowdata, parent, false);
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
                        if (!item.getProjname().toLowerCase().equals(null)) {
                            if (item.getProjname().toLowerCase().contains(filterPattern)) {
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
            holder.txttid.setText(data.get(position).getTid());
            holder.txtprojname.setText(data.get(position).getProjname());
            holder.txtprojdesc.setText(data.get(position).getProjdesc());
            holder.txtnoofhrs.setText(data.get(position).getNoofhrs());
            holder.txtdate.setText(data.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txtprojname, txtprojdesc,txtdate,txttid;
            TextView txtnoofhrs;
            CheckBox checkbox;
            RecyclerViewClickListener mListener;

            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txtprojname = itemView.findViewById(R.id.projname);
                txtprojdesc = itemView.findViewById(R.id.projdesc);
                txtnoofhrs = itemView.findViewById(R.id.timehrs);
                txtdate = itemView.findViewById(R.id.date);
                checkbox = itemView.findViewById(R.id.checkbox);
                txttid = itemView.findViewById(R.id.tid);
                this.mListener = mListener;
                itemView.setOnClickListener(this);

checkbox.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(checkbox.isChecked()){
            checkbox();
        }
        else
            checkboxunchecked();
    }


});


            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v, getAdapterPosition());
//                    Intent intent = new Intent(getContext(),AddTimesheet.class);
//
//                    intent.putExtra("projname",dataAdapter.get(getAdapterPosition()).getProjname());
//                    intent.putExtra("projdesc",dataAdapter.get(getAdapterPosition()).getProjdesc());
//                    intent.putExtra("hrs",dataAdapter.get(getAdapterPosition()).getNoofhrs());
//                    startActivity(intent);
                }
            }
        }

    }
    public class DataAdapter {

        String projname, projdesc,date, noofhrs,tid;

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getProjname() {
            return projname;
        }

        public void setProjname(String projname) {
            this.projname = projname;
        }

        public String getProjdesc() {
            return projdesc;
        }

        public void setProjdesc(String projdesc) {
            this.projdesc = projdesc;
        }

        public String getNoofhrs() {
            return noofhrs;
        }

        public void setNoofhrs(String noofhrs) {
            this.noofhrs = noofhrs;
        }

        public DataAdapter(String tid,String date,String projname, String projdesc, String noofhrs) {
this.tid = tid;
            this.date = date;
            this.projname = projname;
            this.projdesc = projdesc;
            this.noofhrs = noofhrs;
        }


    }
}
