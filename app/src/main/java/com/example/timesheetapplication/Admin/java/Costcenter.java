package com.example.timesheetapplication.Admin.java;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

import com.example.timesheetapplication.AddCostCenter;
import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.Validations;
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
public class Costcenter extends Fragment {
RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
 CustomAdapter adapter;
Spinner txtcostcenter;
EditText txtproj;
Button assignbtn;
View view;
    ImageButton addbtn;
    final List<String> projectlist = new ArrayList<>();
    final List<String> costcenterlist = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Cost Center";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_costcenter, container, false);
        dataAdapter = new ArrayList<>();
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView = view.findViewById(R.id.recyclerview);
        txtcostcenter = view.findViewById(R.id.txtcostcenter);
        txtproj = view.findViewById(R.id.txtproject);
        assignbtn = view.findViewById(R.id.Assignbtn);
        addbtn = view.findViewById(R.id.addcostcenterbtn);

projectlist.add("Select Project");
        costcenterlist.add("Select Cost Center");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, costcenterlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtcostcenter.setAdapter(adapter);


      txtcostcenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          itemchanged();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });


                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(),AddCostCenter.class);
                        intent.putExtra("fragmenttype","Costcenter");
                        startActivityForResult(intent,1);
                    }
                });

        txtproj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), SelectProject.class);
                    if (!txtcostcenter.getSelectedItem().toString().isEmpty() || txtcostcenter.getSelectedItem().toString() != null) {

                        intent.putExtra("costcenter", txtcostcenter.getSelectedItem().toString());

                        startActivityForResult(intent, 2);
                    }


                }catch (Exception e){}
            }
        });




        Firebaselinks.costcenterfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        costcenterlist.add(child.child("costcenter").getValue().toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, costcenterlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txtcostcenter.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });







        assignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> newlist = new ArrayList<>();
if(!txtproj.getText().toString().isEmpty() && !txtcostcenter.getSelectedItem().toString().isEmpty()){
    if (!txtproj.getText().toString().equals("Select Admin") && !txtcostcenter.getSelectedItem().toString().equals("Select Employee/Supervisor")) {

        Firebaselinks.costcenterfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.child("costcenter").getValue().toString().equals(txtcostcenter.getSelectedItem().toString())) {
                            String key = child.getKey();
                            Firebaselinks.costcenterfirebaselink().child(key + "/project").setValue(child.child("project").getValue().toString() + "," + txtproj.getText().toString());

                            Validations.Toast(getContext(), "Assigned Successfully");

                            txtproj.setText("");
                             //txtcostcenter.setSelection(0);
                            DataLoad();



                            return;
                        }


                    }
                    String id = Firebaselinks.costcenterfirebaselink().push().getKey();
                    Firebaselinks.costcenterfirebaselink().child(id).child("costcenter").setValue(txtcostcenter.getSelectedItem().toString());
                    Firebaselinks.costcenterfirebaselink().child(id).child("project").setValue(txtproj.getText().toString());

                    Validations.Toast(getContext(), "Assigned Successfully");
                    txtproj.setText("");
                    DataLoad();



                } else {
                    String id = Firebaselinks.costcenterfirebaselink().push().getKey();
                    Firebaselinks.costcenterfirebaselink().child(id).child("costcenter").setValue(txtcostcenter.getSelectedItem().toString());
                    Firebaselinks.costcenterfirebaselink().child(id).child("project").setValue(txtproj.getText().toString());

                    Validations.Toast(getContext(), "Assigned Successfully");
                    txtproj.setText("");
                    DataLoad();

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

    public void itemchanged(){
        if(dataAdapter.size() > 0)
            dataAdapter.clear();
        Firebaselinks.costcenterfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child  :dataSnapshot.getChildren()){
                        if(child.child("costcenter").getValue().toString().equals(txtcostcenter.getSelectedItem().toString())){
                            if(child.child("project").getValue().toString().contains(",")) {
                                String h[]=child.child("project").getValue().toString().split(",");
                                for(int i=0;i<h.length;i++) {
                                    isi = new DataAdapter(h[i]);
                                    dataAdapter.add(isi);
                                }
                            }
                            else{
                                isi = new DataAdapter(child.child("project").getValue().toString());
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


Firebaselinks.costcenterfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getChildrenCount() > 0){
            for(DataSnapshot child  :dataSnapshot.getChildren()){
                if(child.child("costcenter").getValue().toString().equals(txtcostcenter.getSelectedItem().toString())){
                    if(child.child("project").getValue().toString().contains(",")) {
                        String h[]=child.child("project").getValue().toString().split(",");
                        for(int i=0;i<h.length;i++) {
                            isi = new DataAdapter(h[i]);
                            dataAdapter.add(isi);
                        }
                    }
                    else{
                        isi = new DataAdapter(child.child("project").getValue().toString());
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
        Firebaselinks.costcenterfirebaselink().orderByChild("costcenter").equalTo(txtcostcenter.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("project").getValue().toString().contains(",")){

                            if(child.child("project").getValue().toString().contains(dataAdapter.get(pos).getText())){
                                String[]h= child.child("project").getValue().toString().split(",");
                                for(int i=0;i<h.length;i++) {
                                    projlist.add(h[i]);
                                }

                                for(int i=0;i<projlist.size();i++) {
                                    if (projlist.get(i).equals(dataAdapter.get(pos).getText())) {
                                        projlist.remove(i);
                                    }
                                }
                                String key = child.getKey();
                                Firebaselinks.costcenterfirebaselink().child(key + "/project").setValue(projlist.toString().replace("[", "").replace("]", ""));
                                dataAdapter.remove(pos);
                                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                recyclerView.setAdapter(adapter);
                            }

                        }
                        else{
                            if(child.child("project").getValue().toString().equals(dataAdapter.get(pos).getText())){
                                String key = child.getKey();
                                Firebaselinks.costcenterfirebaselink().child(key + "/project").setValue("");
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK && requestCode==1) {
            super.onActivityResult(requestCode, resultCode, data);

if(data.getStringExtra("result")!= null) {
    costcenterlist.add(data.getStringExtra("result"));
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
            android.R.layout.simple_spinner_item, costcenterlist);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    txtcostcenter.setAdapter(adapter);
    txtcostcenter.setSelection(costcenterlist.size() - 1);
}



        }
        if(resultCode==RESULT_OK && requestCode==2) {
            super.onActivityResult(requestCode, resultCode, data);

            if(data.getStringExtra("resultnames")!= null)
                txtproj.setText(data.getStringExtra("resultnames"));




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
