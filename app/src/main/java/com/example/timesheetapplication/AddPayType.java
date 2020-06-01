package com.example.timesheetapplication;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPayType extends Fragment {
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    View view;
    Button savebtn,editbtn,deletebtn;
    EditText txtpaytype,txtdesc,txtmultiplier;
    RadioButton yesradiobtn,noradiobtn;
    RadioGroup radioGroup;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = "Add Paytype";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_add_pay_type, container, false);
txtpaytype = view.findViewById(R.id.paytype);
txtdesc = view.findViewById(R.id.description);
txtmultiplier= view.findViewById(R.id.multiplier);
savebtn= view.findViewById(R.id.btn);
editbtn= view.findViewById(R.id.editbtn);
deletebtn= view.findViewById(R.id.deletebtn);
        recyclerView = view.findViewById(R.id.recyclerview);
radioGroup = view.findViewById(R.id.radioDefault);
yesradiobtn = view.findViewById(R.id.yesrbt);
noradiobtn = view.findViewById(R.id.norbt);
        dataAdapter = new ArrayList<>();


        Firebaselinks.paytypefirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("default").getValue().toString().equals("Y")){
                            yesradiobtn.setEnabled(false);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





                    listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentviewpaytype fragment = new fragmentviewpaytype();
                Bundle bundle = new Bundle();
                bundle.putString("paytype", dataAdapter.get(position).getPaytype().toString());
                bundle.putString("desc", dataAdapter.get(position).getDesc().toString());
                bundle.putString("multiplier", dataAdapter.get(position).getMultiplier().toString());
                bundle.putString("default", dataAdapter.get(position).getDefaultvalue().toString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame,fragment);
                fragmentTransaction.commit();
            }
        };
DataLoad();




savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        Firebaselinks.paytypefirebaselink().orderByChild("paytype").equalTo(txtpaytype.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    Validations.Toast(getContext(), "Paytype already exists");
                    return;
                }else{


                    if(!txtpaytype.getText().toString().isEmpty() && !txtdesc.getText().toString().isEmpty()
                            && !txtmultiplier.getText().toString().isEmpty()){
                        String id = Firebaselinks.paytypefirebaselink().push().getKey();
                        Firebaselinks.paytypefirebaselink().child(id).child("paytype").setValue(txtpaytype.getText().toString());
                        Firebaselinks.paytypefirebaselink().child(id).child("description").setValue(txtdesc.getText().toString());
                        Firebaselinks.paytypefirebaselink().child(id).child("multiplier").setValue(txtmultiplier.getText().toString());


//                        int selectedId = radioGroup.getCheckedRadioButtonId();
//                        // find the radiobutton by returned id
//                        yesradiobtn = (RadioButton)view.findViewById(selectedId);
                        if(!yesradiobtn.isEnabled())
                            Firebaselinks.paytypefirebaselink().child(id).child("default").setValue("N");
                        else {
                            Firebaselinks.paytypefirebaselink().child(id).child("default").setValue(yesradiobtn.getText().toString());
                            yesradiobtn.setEnabled(false);
                        }

                        yesradiobtn.setChecked(false);
                        Validations.Toast(getContext(),"Pay Type added successfully");
                        Validations.ClearDataWithoutHint(txtpaytype,txtdesc,txtmultiplier);
                        DataLoad();
                    }
                    else
                        Validations.Toast(getContext(),"Please enter all fields");

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });










        }


});


        return view;
    }


    public void Delete(final int position){

Firebaselinks.paytypefirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getChildrenCount() > 0){
            for(DataSnapshot child : dataSnapshot.getChildren()){
                if(child.child("paytype").getValue().toString().equals(dataAdapter.get(position).getPaytype()) &&
                        child.child("multiplier").getValue().toString().equals(dataAdapter.get(position).getMultiplier())){
                    String key = child.getKey();
Firebaselinks.paytypefirebaselink().child(key).removeValue();
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


public void DataLoad(){
        if(dataAdapter.size() > 0)
            dataAdapter.clear();
        Firebaselinks.paytypefirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){

                        isi = new DataAdapter(child.child("paytype").getValue().toString(),child.child("description").getValue().toString(),child.child("multiplier").getValue().toString(),child.child("default").getValue().toString());
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
                        if (!item.getPaytype().toLowerCase().equals(null)) {
                            if (item.getPaytype().toLowerCase().contains(filterPattern)) {
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
            holder.paytype.setText(data.get(position).getPaytype());
            holder.defaultvalue.setText(data.get(position).getDefaultvalue());
            holder.desc.setText(data.get(position).getDesc());
            holder.multiplier.setText(data.get(position).getMultiplier());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
ImageButton deletbtn;
            TextView paytype,desc,multiplier,defaultvalue;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                paytype = itemView.findViewById(R.id.paytype);
                desc = itemView.findViewById(R.id.desc);
                multiplier = itemView.findViewById(R.id.multiplier);
                defaultvalue = itemView.findViewById(R.id.defaultvalue);
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
String paytype,desc,multiplier,defaultvalue;


public DataAdapter(String paytype,String desc,String multiplier,String defaultvalue){
this.paytype = paytype;
this.desc=desc;
this.multiplier=multiplier;
this.defaultvalue=defaultvalue;
}



        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getMultiplier() {
            return multiplier;
        }

        public void setMultiplier(String multiplier) {
            this.multiplier = multiplier;
        }

        public String getDefaultvalue() {
            return defaultvalue;
        }

        public void setDefaultvalue(String defaultvalue) {
            this.defaultvalue = defaultvalue;
        }
    }
}
