package com.example.timesheetapplication.Admin.java;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timesheetapplication.AddCostCenter;
import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.example.timesheetapplication.fragmentviewpaytype;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategory extends Fragment {
View view;
ImageButton addcategorybtn;
Spinner spinnercategory,spinnerproject;
EditText txtrates;
TextView txtenddate;
Button savebtn;
List<String> projectlist = new ArrayList<>();
List<String> categorylist = new ArrayList<>();
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Add Category";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_category, container, false);

        spinnercategory = view.findViewById(R.id.spinnercategory);
        spinnerproject = view.findViewById(R.id.projectspinner);
        txtenddate= view.findViewById(R.id.enddate);
        txtrates= view.findViewById(R.id.rates);
        addcategorybtn = view.findViewById(R.id.addcategory);
        savebtn = view.findViewById(R.id.btn);
        recyclerView = view.findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();

        txtenddate.setText(SetDate.Date(txtenddate,getContext()));


        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                editordeletecatgory fragment = new editordeletecatgory();
                Bundle bundle = new Bundle();
                bundle.putString("category", dataAdapter.get(position).getCategory().toString());
                bundle.putString("project", dataAdapter.get(position).getProject().toString());
                bundle.putString("enddate",Validations.dashtoslash(Validations.yyyytoddwithouttime(Validations.dashtoslash(dataAdapter.get(position).getEnddate().toString()))));
                bundle.putString("rate", dataAdapter.get(position).getRate().toString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame,fragment);
                fragmentTransaction.commit();
            }
        };
        DataLoad();



        Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
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
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
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



        addcategorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCostCenter.class);
                intent.putExtra("fragmenttype","Category");
                startActivityForResult(intent,1);
            }
        });

savebtn.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
       // String date = SetDate.Date(txtenddate,getContext());

String nextyear = SetDate.dateplusoneyear();

        String selecteddate =  Validations.yyyytoddwithouttime(Validations.slashtodash(txtenddate.getText().toString()));
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
        if((d1.compareTo(d2) > 0) && d1.compareTo(nextyeardate) > 0) {
                if(!txtenddate.getText().toString().equals(SetDate.Currentdate())) {

                if (!txtenddate.getText().toString().isEmpty() && !txtrates.getText().toString().isEmpty() && !spinnercategory.getSelectedItem().toString().isEmpty() && !spinnerproject.getSelectedItem().toString().isEmpty()) {


                    String id = Firebaselinks.projectcategoryfirebaselink().push().getKey();
                    Firebaselinks.projectcategoryfirebaselink().child(id).child("project").setValue(spinnerproject.getSelectedItem().toString());
                    Firebaselinks.projectcategoryfirebaselink().child(id).child("category").setValue(spinnercategory.getSelectedItem().toString());


                    Firebaselinks.projectcategoryfirebaselink().child(id).child("enddate").setValue(Validations.slashtodash(Validations.ddtoyyyywithouttime(Validations.slashtodash(txtenddate.getText().toString()))));
                    Firebaselinks.projectcategoryfirebaselink().child(id).child("rates").setValue(txtrates.getText().toString());

                    Validations.ClearDataWithoutHint(txtrates);
                    spinnercategory.setSelection(0);
                    spinnerproject.setSelection(0);
                 //   txtenddate.setText(SetDate.Date(txtenddate,getContext()));
                    Validations.Toast(getContext(),"Category Added");
                    DataLoad();
                }
                else
                    Validations.Toast(getContext(),"Please Enter all fields");

            }
            else
                Validations.Toast(getContext(),"Invalid Date");

        }
        else{
            Validations.Toast(getContext(),"Date is invalid");
            txtenddate.setText(SetDate.Currentdate());
            return;
        }







        }



});






        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK && requestCode==1) {
            super.onActivityResult(requestCode, resultCode, data);

            if(data.getStringExtra("result")!= null) {
                categorylist.add(data.getStringExtra("result"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, categorylist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnercategory.setAdapter(adapter);
                spinnercategory.setSelection(categorylist.size() - 1);
            }



        }

    }

    public void DataLoad(){
        if(dataAdapter.size() > 0)
            dataAdapter.clear();
        Firebaselinks.projectcategoryfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        isi = new DataAdapter(child.child("category").getValue().toString(),child.child("project").getValue().toString(),child.child("enddate").getValue().toString(),child.child("rates").getValue().toString());
                        dataAdapter.add(isi);
                    }

                    Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                    adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    dataAdapter.clear();
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

    public void Delete(final int position){

        Firebaselinks.projectcategoryfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("category").getValue().toString().equals(dataAdapter.get(position).getCategory()) &&
                                child.child("project").getValue().toString().equals(dataAdapter.get(position).getProject()) &&
                                child.child("rates").getValue().toString().equals(dataAdapter.get(position).getRate()) &&
                                child.child("enddate").getValue().toString().equals(dataAdapter.get(position).getEnddate())){
                            String key = child.getKey();
                            Firebaselinks.projectcategoryfirebaselink().child(key).removeValue();
                            dataAdapter.remove(position);
                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                            recyclerView.setAdapter(adapter);
                            return;
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
            View v = LayoutInflater.from(context).inflate(R.layout.addpaytyperowdata, parent, false);
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
                        if (!item.getCategory().toLowerCase().equals(null)) {
                            if (item.getCategory().toLowerCase().contains(filterPattern)) {
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
            holder.category.setText(data.get(position).getCategory());
            holder.project.setText(data.get(position).getProject());
            holder.enddate.setText(data.get(position).getEnddate());
            holder.rates.setText(data.get(position).getRate());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageButton deletbtn;
            TextView category,project,enddate,rates;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                category = itemView.findViewById(R.id.paytype);
                project = itemView.findViewById(R.id.desc);
                enddate = itemView.findViewById(R.id.multiplier);
                rates = itemView.findViewById(R.id.defaultvalue);
                deletbtn = itemView.findViewById(R.id.delete);
                this.mListener=mListener;
                itemView.setOnClickListener(this);

                deletbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Delete(getAdapterPosition());
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
        String category,project,enddate,rate;


        public DataAdapter(String category,String project,String enddate,String rate){
            this.category = category;
            this.project=project;
            this.enddate=enddate;
            this.rate=rate;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }
}
