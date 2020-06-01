package com.example.timesheetapplication.Employee.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class AddTimesheetMainPage extends Fragment {
    ImageButton addtimesheetbtn, leftarrowbtn, rightarrowbtn;
    EditText txtdate,txtdesc;
    RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
Spinner addpaytypespinner;
    Button savebtn;
    TextView  txttotalhrs;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;

    LinearLayout addtimesheetllt;
int tid;
public String empid,empname,type,logintype;
    Spinner selectprojectspinner;
EditText txtaddhrs;
    final List<String> itemchild = new ArrayList<>();
    final List<String> paytypelist = new ArrayList<>();
View view;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Add Timesheet";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.activity_add_timesheet_main_page, container, false);
try {
    dataAdapter = new ArrayList<>();
    txttotalhrs = view.findViewById(R.id.totalhrs);
    txtaddhrs = view.findViewById(R.id.txtaddhrs);
    txtdesc = view.findViewById(R.id.txtdesc);
    selectprojectspinner = view.findViewById(R.id.selectprojectspinner);
    addtimesheetllt = view.findViewById(R.id.addtimesheetllt);
    addtimesheetbtn = view.findViewById(R.id.addtimesheetbtn);
    savebtn = view.findViewById(R.id.Savebtn);
    leftarrowbtn = view.findViewById(R.id.leftarrow);
    rightarrowbtn = view.findViewById(R.id.rightarrow);
    txtdate = view.findViewById(R.id.date);
    recyclerView = view.findViewById(R.id.recyclerview);
    addpaytypespinner = view.findViewById(R.id.addpaytype);
    savebtn.setVisibility(View.INVISIBLE);


    Firebaselinks.paytypefirebaselink().orderByChild("default").equalTo("Y").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        paytypelist.add(child.child("paytype").getValue().toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, paytypelist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    addpaytypespinner.setAdapter(adapter);
                }
            }catch(Exception e){}
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });


    Firebaselinks.paytypefirebaselink().orderByChild("default").equalTo("N").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try{
            if(dataSnapshot.getChildrenCount() > 0){
                for(DataSnapshot child :dataSnapshot.getChildren()){
                    paytypelist.add(child.child("paytype").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, paytypelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addpaytypespinner.setAdapter(adapter);

                addpaytypespinner.setSelection(0);
                String g = addpaytypespinner.getSelectedItem().toString();
            }
            }catch(Exception e){}
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });







    listener = new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, int position) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            AddTimesheet fragment = new AddTimesheet();
            Bundle bundle = new Bundle();
            bundle.putString("projname", dataAdapter.get(position).getProjname().toString());
            bundle.putString("projdesc", dataAdapter.get(position).getProjdesc().toString());
            bundle.putString("desc", dataAdapter.get(position).getdesc().toString());
            bundle.putString("hrs", dataAdapter.get(position).getNoofhrs().toString());
            bundle.putString("tid", dataAdapter.get(position).getTid().toString());
            bundle.putString("paytype", dataAdapter.get(position).getPaytype().toString());

            fragment.empid = empid;
            bundle.putString("type", "arrow");
            fragment.setArguments(bundle);

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


    addtimesheetbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addbtnclick();
        }
    });

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

    txtaddhrs.setEnabled(false);

    txtdate.setText(SetDate.withoutonclickDate(txtdate, getContext()));
    Date d2;
    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
    try {
        Date textdate = sdformat.parse(Validations.ddtoyyyywithouttime(Validations.slashtodash(txtdate.getText().toString())));
        d2 = sdformat.parse(Validations.ddtoyyyywithouttime(SetDate.dateminus(SetDate.Currentdate(), 7)));
        if (textdate.after(d2)) {
            addtimesheetllt.setVisibility(View.VISIBLE);
            addtimesheetbtn.setVisibility(View.VISIBLE);
        } else if (textdate.equals(d2)) {
            addtimesheetllt.setVisibility(View.VISIBLE);
            addtimesheetbtn.setVisibility(View.VISIBLE);
        } else {
            addtimesheetllt.setVisibility(View.GONE);
            addtimesheetbtn.setVisibility(View.GONE);
        }


    } catch (ParseException e) {
        e.printStackTrace();
    }


    tid = maxtimesheetid();

    LoadProjects();

    DataLoad();


    selectprojectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            txtaddhrs.setEnabled(true);
            savebtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


    savebtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savemethod();
        }

    });


}catch(Exception e){}
return view;

    }


    public void savemethod() {
        final String id = Firebaselinks.timesheetfirebaselink().push().getKey();


        if (!txtaddhrs.getText().toString().isEmpty() && !selectprojectspinner.getSelectedItem().toString().isEmpty()) {
            Firebaselinks.timesheetfirebaselink().child(id).child("timesheetid").setValue(maxtimesheetid());
            Firebaselinks.timesheetfirebaselink().child(id).child("date").setValue(Validations.ddtoyyyywithtime(txtdate.getText().toString()));


            Firebaselinks.projectsfirebaselink().orderByChild("projname").equalTo(selectprojectspinner.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        for(DataSnapshot child :dataSnapshot.getChildren()){
                            Firebaselinks.timesheetfirebaselink().child(id).child("projid").setValue(child.child("projid").getValue().toString());
                            Firebaselinks.timesheetfirebaselink().child(id).child("noofhrs").setValue(txtaddhrs.getText().toString());
                            Firebaselinks.timesheetfirebaselink().child(id).child("empid").setValue(empid);
                            Firebaselinks.timesheetfirebaselink().child(id).child("paytype").setValue(addpaytypespinner.getSelectedItem().toString());
                            Firebaselinks.timesheetfirebaselink().child(id).child("desc").setValue(txtdesc.getText().toString());
                            Firebaselinks.timesheetfirebaselink().child(id).child("status").setValue("Complete");
                            Firebaselinks.timesheetfirebaselink().child(id).child("confirm").setValue("no");
                           // txtaddhrs.setEnabled(false);
                         //   savebtn.setVisibility(View.INVISIBLE);
                        }
                        DataLoad();
                        if(!txttotalhrs.getText().toString().isEmpty())
                        {
                            if(txttotalhrs.getText().toString().equals("0")){
                                txttotalhrs.setText(txtaddhrs.getText().toString());
                            }
                            else
                                txttotalhrs.setText(Float.parseFloat(txttotalhrs.getText().toString()) + Float.parseFloat(txtaddhrs.getText().toString()) + "");


                        }

                        txtaddhrs.setText("");
                        txtdesc.setText("");
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });



        }




    }
    public void LoadProjects(){
        try {
            final List<String> finalitemchild = new ArrayList<>();
            Firebaselinks.assignedprojectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        try {
                            for (final DataSnapshot child : dataSnapshot.getChildren()) {

                                if (child.child("assignedids").getValue().toString().contains(",")) {
                                    if (child.child("assignedids").getValue().toString().contains(empid)) {
                                        itemchild.add(child.child("projid").getValue().toString());
                                    }
                                } else {
                                    if (child.child("assignedids").getValue().toString().equals(empid)) {
                                        itemchild.add(child.child("projid").getValue().toString());
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                        Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    try {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                            if (itemchild.contains(dataSnapshot2.child("projid").getValue().toString())) {
                                                finalitemchild.add(dataSnapshot2.child("projname").getValue().toString());
                                            }
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                                android.R.layout.simple_spinner_item, finalitemchild);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        selectprojectspinner.setAdapter(adapter);
                                    } catch (Exception e) {
                                    }
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
        }catch(Exception e){}
    }



    public void DataLoad(){
        try {
            if (dataAdapter.size() > 0)
                dataAdapter.clear();


            final List<String> itemchild = new ArrayList<>();
            final List<Float> totalhrs = new ArrayList<>();

            Firebaselinks.assignedprojectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (final DataSnapshot child : dataSnapshot.getChildren()) {

                            if (child.child("assignedids").getValue().toString().contains(",")) {
                                try {
                                    if (child.child("assignedids").getValue().toString().contains(empid)) {


                                        Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(child.child("projid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getChildrenCount() > 0) {
                                                    for (DataSnapshot projchild : dataSnapshot.getChildren()) {
                                                        itemchild.add(child.child("projid").getValue().toString() + "_:" +
                                                                projchild.child("projname").getValue().toString() + "_:" +
                                                                projchild.child("projdesc").getValue().toString()
                                                        );
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                if (child.child("assignedids").getValue().toString().equals(empid)) {
                                    Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(child.child("projid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getChildrenCount() > 0) {
                                                for (DataSnapshot projchild : dataSnapshot.getChildren()) {
                                                    itemchild.add(child.child("projid").getValue().toString() + "_:" +
                                                            projchild.child("projname").getValue().toString() + "_:" +
                                                            projchild.child("projdesc").getValue().toString()
                                                    );
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });

                                }
                            }
                        }


                        Firebaselinks.timesheetfirebaselink().orderByChild("empid").equalTo(empid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    dataAdapter.clear();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (Validations.newyyyytoddwithouttime(child.child("date").getValue().toString()).equals(Validations.slashtodash(txtdate.getText().toString()))) {


                                            for (int i = 0; i < itemchild.size(); i++){
                                                if (itemchild.get(i).split("_:")[0].equals(child.child("projid").getValue().toString())) {
                                                    isi = new DataAdapter(child.child("timesheetid").getValue().toString(), itemchild.get(i).split("_:")[1], child.child("desc").getValue().toString(), child.child("noofhrs").getValue().toString(), itemchild.get(i).split("_:")[2],child.child("paytype").getValue().toString());
                                                    dataAdapter.add(isi);
                                                    totalhrs.add(Float.parseFloat(child.child("noofhrs").getValue().toString()));
                                                }
                                        }

                                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                            recyclerView.setAdapter(adapter);

                                            Float sum = 0f;
                                            //Advanced for loop
                                            for (Float num : totalhrs) {
                                                sum = sum + num;
                                            }
                                            txttotalhrs.setText(Math.round(sum) + "");

                                        }
                                        if (dataAdapter.size() == 0)
                                            txttotalhrs.setText("0");
                                    }


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


        }catch(Exception e){}


    }



public int maxtimesheetid(){
    Firebaselinks.timesheetfirebaselink().
            addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            tid = Integer.parseInt(childSnapshot.child("timesheetid").getValue().toString());
                        }
                        tid = tid + 1;
                    }
                    else
                        tid = 1;
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }


            });
return tid;
}










    public void addbtnclick() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        AllProjectsAssigned fragment = new AllProjectsAssigned();
      fragment.empid = empid;
        fragment.type = type;
        fragment.date = txtdate.getText().toString();

        fragmentTransaction.addToBackStack(null);
        if(logintype!= null) {

                if (logintype.equals("Admin"))
                    fragmentTransaction.replace(R.id.content_frame, fragment);
            fragment.logintype = "Admin";
        }
        else
            fragmentTransaction.replace(R.id.fragmentcontainer,fragment);
        fragmentTransaction.commit();
    }

   public void leftrightarrowclick(String arrowclick){
if(arrowclick.equals("Left")){
    dataAdapter.clear();

    Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
    adapter = new CustomAdapter(getContext(), dataAdapter, listener);
    recyclerView.setAdapter(adapter);
    txtdate.setText(SetDate.dateminusone(txtdate));
    Date d2;
    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
    try {
        Date textdate = sdformat.parse(Validations.ddtoyyyywithouttime(Validations.slashtodash(txtdate.getText().toString())));
        d2 =sdformat.parse(Validations.ddtoyyyywithouttime(SetDate.dateminus(SetDate.Currentdate(),7)));

//            Date d1 = sdformat.parse("2020-04-29");
//            Date d2 = sdformat.parse("2020-04-22");
//
        if(textdate.after(d2)){
            addtimesheetllt.setVisibility(View.VISIBLE);
            addtimesheetbtn.setVisibility(View.VISIBLE);
        }
        else if(textdate.equals(d2)){
            addtimesheetllt.setVisibility(View.VISIBLE);
            addtimesheetbtn.setVisibility(View.VISIBLE);
        }

        else {
            addtimesheetllt.setVisibility(View.GONE);
            addtimesheetbtn.setVisibility(View.GONE);
        }


    } catch (ParseException e) {
        e.printStackTrace();
    }
    DataLoad();
}

       if(arrowclick.equals("Right")){
           if (!SetDate.Currentdate().equals(txtdate.getText().toString())) {

               dataAdapter.clear();


               Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
               adapter = new CustomAdapter(getContext(), dataAdapter, listener);
               recyclerView.setAdapter(adapter);
               txtdate.setText(SetDate.dateplusone(txtdate));
               Date d2;
               SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
               try {
                   Date textdate = sdformat.parse(Validations.ddtoyyyywithouttime(Validations.slashtodash(txtdate.getText().toString())));
                   d2 =sdformat.parse(Validations.ddtoyyyywithouttime(SetDate.dateminus(SetDate.Currentdate(),7)));

//            Date d1 = sdformat.parse("2020-04-29");
//            Date d2 = sdformat.parse("2020-04-22");
//
                   if(textdate.after(d2)){
                       addtimesheetllt.setVisibility(View.VISIBLE);
                       addtimesheetbtn.setVisibility(View.VISIBLE);
                   }
                   else if(textdate.equals(d2)){
                       addtimesheetllt.setVisibility(View.VISIBLE);
                       addtimesheetbtn.setVisibility(View.VISIBLE);
                   }

                   else {
                       addtimesheetllt.setVisibility(View.GONE);
                       addtimesheetbtn.setVisibility(View.GONE);
                   }


               } catch (ParseException e) {
                   e.printStackTrace();
               }
               DataLoad();
           }

       }
   }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.timesheetmainpagerowdata, parent, false);
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
            holder.txtprojname.setText(data.get(position).getProjname());
            holder.txtdesc.setText(data.get(position).getdesc());
            holder.txtnoofhrs.setText(data.get(position).getNoofhrs());
            holder.txtprojdesc.setText(data.get(position).getProjdesc());
            holder.txtpaytype.setText(data.get(position).getPaytype());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txtprojname,txtprojdesc, txtdesc,txtid,txtpaytype;

            ImageView btnrightarrow;
            TextView txtnoofhrs;
            RecyclerViewClickListener mListener;

            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txtprojname = itemView.findViewById(R.id.name);
                txtprojdesc = itemView.findViewById(R.id.projdesc);
                txtdesc = itemView.findViewById(R.id.desc);
                txtid = itemView.findViewById(R.id.timesheetid);
                txtpaytype = itemView.findViewById(R.id.paytype);
                txtnoofhrs = itemView.findViewById(R.id.noofhrs);
                btnrightarrow = itemView.findViewById(R.id.rightarrow);
                this.mListener = mListener;
                itemView.setOnClickListener(this);



                btnrightarrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        AddTimesheet fragment = new AddTimesheet();
                        Bundle bundle = new Bundle();
                        bundle.putString("projname", dataAdapter.get(getAdapterPosition()).getProjname().toString());
                        bundle.putString("projdesc", dataAdapter.get(getAdapterPosition()).getProjdesc().toString());
                        bundle.putString("desc", dataAdapter.get(getAdapterPosition()).getdesc().toString());
                        bundle.putString("hrs", dataAdapter.get(getAdapterPosition()).getNoofhrs().toString());
                        bundle.putString("tid", dataAdapter.get(getAdapterPosition()).getTid().toString());
                        bundle.putString("paytype", dataAdapter.get(getAdapterPosition()).getPaytype().toString());
                        fragment.empid = empid;

                        bundle.putString("type","arrow");
                        fragment.setArguments(bundle);

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
                });

            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v, getAdapterPosition());
                }
            }
        }

    }
        public class DataAdapter {

            String projname, desc, noofhrs,tid,projdesc,paytype;

            public String getPaytype() {
                return paytype;
            }

            public void setPaytype(String paytype) {
                this.paytype = paytype;
            }

            public String getProjdesc() {
                return projdesc;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getProjname() {
                return projname;
            }

            public void setProjname(String projname) {
                this.projname = projname;
            }

            public String getdesc() {
                return desc;
            }

            public void setProjdesc(String desc) {
                this.desc = desc;
            }

            public String getNoofhrs() {
                return noofhrs;
            }

            public void setNoofhrs(String noofhrs) {
                this.noofhrs = noofhrs;
            }

            public DataAdapter(String tid,String projname, String desc, String noofhrs,String projdesc,String paytype) {
                this.tid = tid;
                this.projname = projname;
                this.desc = desc;
                this.noofhrs = noofhrs;
                this.projdesc = projdesc;
                this.paytype= paytype;
            }


        }

}
