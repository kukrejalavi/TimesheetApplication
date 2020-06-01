package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddnewProject extends Fragment {
    Toolbar toolbar;
    TextView txtcountertext;
    TextView txtdate,txtprojid;
    MultiSelectCheckboxSpinner multiSelectCheckboxSpinner = new MultiSelectCheckboxSpinner();
    Button assignempspinnerbtn;
    EditText txtprojname,txtprojdesc;
     String selectedids;
    int Projid;
    View view;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    Button savebtn,deletebtn,editbtn;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Add Project";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.activity_addnew_project, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();

        txtdate = view.findViewById(R.id.date);
        savebtn = view.findViewById(R.id.save);
        deletebtn = view.findViewById(R.id.delete);
        editbtn = view.findViewById(R.id.edit);
        txtprojid = view.findViewById(R.id.projid);
        txtprojname =view. findViewById(R.id.projname);
        txtprojdesc =view. findViewById(R.id.desc);
        assignempspinnerbtn = view.findViewById(R.id.empnamebtn);
        txtdate.setText(SetDate.withoutonclickDate(txtdate,getContext()));
        deletebtn.setVisibility(View.GONE);
        editbtn.setVisibility(View.GONE);
        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            assignempspinnerbtn.setVisibility(View.GONE);
            savebtn.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
          //  deletebtn.setVisibility(View.VISIBLE);
                      txtprojname.setEnabled(true);
            txtprojdesc.setEnabled(true);

            txtprojname.setText(bundle.getString("projname"));

            Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(bundle.getString("projid")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            txtdate.setText(Validations.dashtoslash(Validations.newyyyytoddwithouttime(child.child("date").getValue().toString())));
                          txtprojid.setText(child.child("projid").getValue().toString());
                            txtprojdesc.setText(child.child("projdesc").getValue().toString());
                        }


                   Firebaselinks.timesheetfirebaselink().orderByChild("projid").equalTo(bundle.getString("projid")).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if(dataSnapshot.getChildrenCount() >0){
                                deletebtn.setVisibility(View.GONE);
                                editbtn.setVisibility(View.GONE);
                           }
                           else {
                               deletebtn.setVisibility(View.VISIBLE);
                               editbtn.setVisibility(View.VISIBLE);
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



            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(bundle.getString("projid")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() > 0){
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    String clubkey = child.getKey();
                                    Firebaselinks.projectsfirebaselink().child(clubkey + "/projname").setValue(txtprojname.getText().toString());
                                    Firebaselinks.projectsfirebaselink().child(clubkey + "/projdesc").setValue(txtprojdesc.getText().toString());
                                    Toast.makeText(getContext(),"Project edited Successfully",Toast.LENGTH_SHORT).show();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    ProjectList fragment = new ProjectList();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.replace(R.id.content_frame,fragment);
                                    fragmentTransaction.commit();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            });
            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Firebaselinks.timesheetfirebaselink().orderByChild("projid").equalTo(bundle.getString("projid")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() > 0){
                                Validations.Toast(getContext(),"Cannot delete project");
                                return;
                            }
                            else{

                                Firebaselinks.projectsfirebaselink().orderByChild("projid").equalTo(bundle.getString("projid")).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getChildrenCount() > 0){
                                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                                String clubkey = child.getKey();
                                                Firebaselinks.projectsfirebaselink().child(clubkey + "/projstatus").setValue("Inactive");
                                                Toast.makeText(getContext(),"Project deleted Successfully",Toast.LENGTH_SHORT).show();
                                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                ProjectList fragment = new ProjectList();
                                                FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.replace(R.id.content_frame,fragment);
                                                fragmentTransaction.commit();
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




          }
            });


        }
else {
            maxprojid();
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savemethod();
                }
            });
            assignempspinnerbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView.getVisibility() == View.VISIBLE && dataAdapter.size() > 0) {
                        List<String> items = new ArrayList<>();
                        List<String> ids = new ArrayList<>();
                        List<String> desc = new ArrayList<>();
                        for (int i = 0; i < dataAdapter.size(); i++) {
                            items.add(dataAdapter.get(i).getEmp());
                            ids.add(dataAdapter.get(i).getId());
                            desc.add(dataAdapter.get(i).getDesc());
                        }

                        Intent intent = new Intent(getContext(), multiSelectCheckboxSpinner.getClass());

                        intent.putExtra("emp", items.toString());
                        intent.putExtra("desc", desc.toString());
                        intent.putExtra("ids", ids.toString());
                        startActivityForResult(intent, 1);


                    } else
                        startActivityForResult(new Intent(getContext(), multiSelectCheckboxSpinner.getClass()), 1);

                }
            });

        }


        return view;
    }


    public void maxprojid(){
        Firebaselinks.projectsfirebaselink().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                Projid = Integer.parseInt(childSnapshot.child("projid").getValue().toString());
                            }
                            txtprojid.setText(Projid + 1 + "");
                        }
                        else
                            txtprojid.setText("1");
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }


                });

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if(resultCode==RESULT_OK && requestCode==1){
            super.onActivityResult(requestCode, resultCode, data);
            final String selecteditems = data.getStringExtra("result");
          selectedids = data.getStringExtra("resultids");
         //   final String  selecteddesc = data.getStringExtra("resultdesc");


            recyclerView.setVisibility(View.VISIBLE);

            String[] items = selecteditems.split(",");
            String[] ids = selectedids.split(",");
       //     String[] desc = selecteddesc.split(",");
for(int i=0;i<items.length;i++) {
    isi = new DataAdapter(items[i],ids[i],"");
    dataAdapter.add(isi);
}


            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
            recyclerView.setAdapter(adapter);

        }
    }


    public void deleteemp(int position){

        dataAdapter.remove(dataAdapter.get(position));

        Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);

    }

    public void savemethod() {
        String id = Firebaselinks.projectsfirebaselink().push().getKey();

        if (!Validations.gettexttostring(txtprojname).isEmpty()) {
            if (!Validations.gettexttostring(txtprojdesc).isEmpty()) {

                Firebaselinks.projectsfirebaselink().child(id).child("date").setValue(Validations.ddtoyyyywithtime(txtdate.getText().toString()));
                Firebaselinks.projectsfirebaselink().child(id).child("projid").setValue(txtprojid.getText().toString());
                Firebaselinks.projectsfirebaselink().child(id).child("projname").setValue(txtprojname.getText().toString());
                Firebaselinks.projectsfirebaselink().child(id).child("projdesc").setValue(txtprojdesc.getText().toString());
                Firebaselinks.projectsfirebaselink().child(id).child("projstatus").setValue("Active");
                List<String> ids = new ArrayList<>();
                List<String> desc = new ArrayList<>();
                if (dataAdapter.size() > 0) {
                    for (int i = 0; i < dataAdapter.size(); i++) {
                        ids.add(dataAdapter.get(i).getId());
                        desc.add(dataAdapter.get(i).getDesc());
                    }

                    String id1 = Firebaselinks.assignedprojectsfirebaselink().push().getKey();
                    Firebaselinks.assignedprojectsfirebaselink().child(id1).child("date").setValue(Validations.ddtoyyyywithtime(txtdate.getText().toString()));
                    Firebaselinks.assignedprojectsfirebaselink().child(id1).child("projid").setValue(txtprojid.getText().toString());
                    Firebaselinks.assignedprojectsfirebaselink().child(id1).child("assignedby").setValue("Admin");
                    Firebaselinks.assignedprojectsfirebaselink().child(id1).child("assignedids").setValue(ids.toString().replace("[", "").replace("]", ""));
                    Firebaselinks.assignedprojectsfirebaselink().child(id1).child("assigneddesc").setValue(desc.toString().replace("[", "").replace("]", ""));
                }
                Toast.makeText(getContext(), "Project Added Successfully", Toast.LENGTH_SHORT).show();
                Validations.ClearDataWithoutHint(txtprojname, txtprojdesc);
                dataAdapter.clear();
                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                recyclerView.setAdapter(adapter);

                maxprojid();
                // getActivity().finish();
            }
            else{
                Toast.makeText(getContext(),"Please Enter Project Description",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(),"Please Enter Project Name",Toast.LENGTH_SHORT).show();
        }
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;
        Boolean isSearched = false;
        public CustomAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.twotextviewsrowdata, parent, false);
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
//                        if (!item.getText().toLowerCase().equals(null)) {
////                            if(item.getChk())
////                                item.setChk(true);
////                            else
////                                item.setChk(true);
////
//                            if (item.getText().toLowerCase().contains(filterPattern)) {
////
//                                if(!item.getSenddesctoemp().toLowerCase().isEmpty())
//                                    item.setSenddesctoemp(item.getSenddesctoemp().toString());
//                                else
//                                    item.setSenddesctoemp("");
//
//
//                                filteredList.add(item);
//
//                            }
//                        }
                    }
                    // int h = items.size();
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
           //     isSearched = true;
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
                holder.txtemp.setText(data.get(position).getEmp());
                holder.txtdesc.setText(data.get(position).getDesc());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView txtemp,txtdesc,txtempid;
            ImageView crossbtn;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txtemp = itemView.findViewById(R.id.first);
                txtdesc = itemView.findViewById(R.id.second);
                txtempid = itemView.findViewById(R.id.optional);
                crossbtn = itemView.findViewById(R.id.crossbtn);
                crossbtn.setVisibility(View.VISIBLE);
                this.mListener=mListener;
                itemView.setOnClickListener(this);

                crossbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteemp(getAdapterPosition());
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

        String emp,desc,id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmp() {
            return emp;
        }

        public void setEmp(String emp) {
            this.emp = emp;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public DataAdapter(String emp,String id, String desc) {
            this.emp =emp;
            this.id = id;
            this.desc=desc;
        }

    }
}
