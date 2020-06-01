package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.timesheetapplication.FirebaseContext;
import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.Shortcuts;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class Assignprojandsv extends Fragment {
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    View view;
    EditText txtemp;
    AppCompatSpinner txtsv;
    Button assignbtn;
    public String type;
    TextView txtids,txtfirst;

    String selecteditems,selectedids,selecteddesc;


    final List<String> spinneritemswithids = new ArrayList<>();
    final List<String> spinneritemswithoutids = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Assign";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_assignprojandsupervisor, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();
txtemp = view.findViewById(R.id.txtemp);
txtfirst = view.findViewById(R.id.First);
txtsv = view.findViewById(R.id.txtsv);
        assignbtn = view.findViewById(R.id.Assignbtn);
txtids = view.findViewById(R.id.ids);



if(type.equals("Supervisor")) {
    txtfirst.setText("Select Supervisor");
    Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getChildrenCount() > 0) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.child("status").getValue().toString().equals("Active") && child.child("profile").getValue().toString().equals("Supervisor")) {
                            spinneritemswithids.add(Shortcuts.childgetvalue(child, "name"));
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, spinneritemswithids);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txtsv.setAdapter(adapter);

                    LoadRecyclerview();
                }catch(Exception e){}
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });
}
else{
    txtfirst.setText("Select Project");
    try {
        Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.child("projstatus").getValue().toString().equals("Active")) {
                            spinneritemswithids.add(Shortcuts.childgetvalue(child, "projname") + "_" + Shortcuts.childgetvalue(child, "projid"));
                        }
                    }

                    for (int i = 0; i < spinneritemswithids.size(); i++) {
                        String[] h = spinneritemswithids.get(i).split("_");
                        spinneritemswithoutids.add(h[0]);
                    }

                    txtids.setText(spinneritemswithids.get(0).split("_")[1]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, spinneritemswithoutids);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txtsv.setAdapter(adapter);

                    LoadRecyclerview();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }catch(Exception e){}
}



txtsv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        try {
                                            int txtpos = txtsv.getSelectedItemPosition();
                                            if (!txtids.getText().toString().isEmpty())
                                                txtids.setText("");
                                        if(!type.equals("Supervisor"))
//                                            txtids.setText(spinneritemswithids.get(txtpos).split("_")[1]);
//                                        else
                                            txtids.setText(spinneritemswithids.get(txtpos).split("_")[1]);
LoadRecyclerview();
                                        }catch(Exception e){}
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


        txtemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectEmps.class);
                try {

                    if (!txtsv.getSelectedItem().toString().isEmpty() || txtsv.getSelectedItem().toString() != null) {
                    if(type.equals("Supervisor"))
                        intent.putExtra("svname", txtsv.getSelectedItem().toString());
                    else
                        intent.putExtra("projid",txtids.getText().toString());

                        startActivityForResult(intent, 1);
                    }
                }catch (Exception e){}
            }
        });

assignbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (dataAdapter.size() > 0)
            dataAdapter.clear();
        if (type.equals("Supervisor")) {
            if (!txtemp.getText().toString().isEmpty() && !txtsv.getSelectedItem().toString().isEmpty()) {


                Firebaselinks.assignsvtoemp().orderByChild("Supervisor").equalTo(txtsv.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
for(DataSnapshot child : dataSnapshot.getChildren()){
    String emp = child.child("Employee").getValue().toString() +"," + txtemp.getText().toString();
    String empids = child.child("Employeeids").getValue().toString() +"," + selectedids;

    String clubkey = child.getKey();
    Firebaselinks.assignsvtoemp().child(clubkey +"/Employee").setValue(emp);
    Firebaselinks.assignsvtoemp().child(clubkey +"/Employeeids").setValue(empids);
    txtemp.setText("");
    Toast.makeText(getContext(),"Assigned Successfully",Toast.LENGTH_SHORT).show();
    LoadRecyclerview();
}
                        }else{
                            String id = Firebaselinks.assignsvtoemp().push().getKey();
                            Firebaselinks.assignsvtoemp().child(id).child("Supervisor").setValue(txtsv.getSelectedItem().toString());
                            Firebaselinks.assignsvtoemp().child(id).child("Employee").setValue(txtemp.getText().toString());
                            Firebaselinks.assignsvtoemp().child(id).child("Employeeids").setValue(selectedids);
                            txtemp.setText("");
                            Toast.makeText(getContext(),"Assigned Successfully",Toast.LENGTH_SHORT).show();
                            LoadRecyclerview();
                        }

                     //   DataLoad("Supervisor");

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

        }
        if (type.equals("Project")) {
            if (!txtemp.getText().toString().isEmpty() && !txtsv.getSelectedItem().toString().isEmpty()
            && !txtids.getText().toString().isEmpty()
            ) {

                Firebaselinks.assignedprojectsfirebaselink().orderByChild("projid").equalTo(txtids.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                String emp = child.child("assignedids").getValue().toString() +"," + selectedids.toString();
                                String empname = child.child("assignednames").getValue().toString() +"," + txtemp.getText().toString();
                                String clubkey = child.getKey();
                                Firebaselinks.assignedprojectsfirebaselink().child(clubkey +"/assignedids").setValue(emp);
                                Firebaselinks.assignedprojectsfirebaselink().child(clubkey +"/assignednames").setValue(empname);
                                txtemp.setText("");
                                Toast.makeText(getContext(),"Assigned Successfully",Toast.LENGTH_SHORT).show();
                                LoadRecyclerview();
                            }
                        }
                        else{
                            String id = Firebaselinks.assignedprojectsfirebaselink().push().getKey();
                            Firebaselinks.assignedprojectsfirebaselink().child(id).child("date").setValue(Validations.newyyyytoddwithouttime(Validations.currentDate()));
                            Firebaselinks.assignedprojectsfirebaselink().child(id).child("projid").setValue(txtids.getText().toString());
                            Firebaselinks.assignedprojectsfirebaselink().child(id).child("assignedids").setValue(selectedids.toString().replace("[", "").replace("]", ""));
                            Firebaselinks.assignedprojectsfirebaselink().child(id).child("assignednames").setValue(txtemp.getText().toString());
                            Firebaselinks.assignedprojectsfirebaselink().child(id).child("assignedby").setValue("Admin");

                            txtemp.setText("");
                            Toast.makeText(getContext(),"Assigned Successfully",Toast.LENGTH_SHORT).show();
                            LoadRecyclerview();
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


        return view;
    }


    public void LoadRecyclerview() {
        final String sv = txtsv.getSelectedItem().toString();

        if (type.equals("Supervisor")) {

            if (dataAdapter.size() > 0)
                dataAdapter.clear();

            if (sv != null || sv != "" || !sv.isEmpty()) {
                Firebaselinks.assignsvtoemp().orderByChild("Supervisor").equalTo(sv).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {

                            if (dataAdapter.size() > 0)
                                dataAdapter.clear();

                            for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                            //    if (childsnapshot.child("Supervisor").getValue().toString().equals(sv)) {
                                    if (childsnapshot.child("Employee").getValue().toString().contains(",")) {
                                        String[] h = childsnapshot.child("Employee").getValue().toString().split(",");
                                        //     nameslist.toString().replace("[", "").replace("]", "")
                                        for (int i = 0; i < h.length; i++) {
                                            isi = new DataAdapter(h[i], "", "");
                                            dataAdapter.add(isi);
                                        }
                                    } else {
                                        isi = new DataAdapter(childsnapshot.child("Employee").getValue().toString(), "", "");
                                        dataAdapter.add(isi);
                                    }

                               // }

                            }
                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                            recyclerView.setAdapter(adapter);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }


        if (type.equals("Project")) {
            if (dataAdapter.size() > 0)
                dataAdapter.clear();
            final List<String> demo = new ArrayList<>();
            if (sv != null || sv != "" || !sv.isEmpty()) {
                final int txtpos = txtsv.getSelectedItemPosition();
                Firebaselinks.assignedprojectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (final DataSnapshot child : dataSnapshot.getChildren()) {
                                if (child.child("projid").getValue().toString().equals(spinneritemswithids.get(txtpos).split("_")[1])) {
                                    if (child.child("assignednames").getValue().toString().contains(",")) {
                                        final String[] h = child.child("assignednames").getValue().toString().split(",");
                                        for (int i = 0; i < h.length; i++) {
                                            isi = new DataAdapter(h[i], "", "");
                                            dataAdapter.add(isi);

                                        }

                                    } else {
                                        isi = new DataAdapter(child.child("assignednames").getValue().toString(), "", "");
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
        }
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if(resultCode==RESULT_OK && requestCode==1){
            super.onActivityResult(requestCode, resultCode, data);
              selecteditems = data.getStringExtra("result");
                 selectedids = data.getStringExtra("resultids");
            //   selecteddesc = data.getStringExtra("resultdesc");

               txtemp.setText(selecteditems);


        }
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
                id = itemView.findViewById(R.id.id);
                role = itemView.findViewById(R.id.role);
                role.setVisibility(View.GONE);
                clearbtn = itemView.findViewById(R.id.clearbtn);
                clearbtn.setVisibility(View.GONE);
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


        public DataAdapter(String text,String id,String role) {
            this.text =text;
            this.id=id;
            this.role= role;
        }

    }
}
