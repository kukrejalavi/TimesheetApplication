package com.example.timesheetapplication.Admin.java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class MultiSelectCheckboxSpinner extends AppCompatActivity {
AppCompatEditText txtselecteditems,txtselectedids,txtselecteddesc;
RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    AppCompatEditText txtsearch;
    Boolean ischecked;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_select_checkbox_spinner);
        txtselecteditems = findViewById(R.id.selecteditems);
        txtselectedids = findViewById(R.id.selectedids);
        txtselecteddesc = findViewById(R.id.selecteddesc);
        recyclerView = findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();
txtsearch = findViewById(R.id.searchtxt);
        DataLoad();
        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter!= null)
                    adapter.getFilter().filter(s);





            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });








        Validations.setRecyclerviewLayoutManager(recyclerView, getApplicationContext());
        adapter = new CustomAdapter(getApplicationContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);
        
    }








    public void DataLoad(){

        dataAdapter = new ArrayList<>();
        Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.child("status").getValue().toString().equals("Active")) {

                            Intent intent = getIntent();
                            if(intent.getStringExtra("emp") != null) {
                                if (intent.getStringExtra("emp") != null || intent.getStringExtra("emp") != "") {
                                    if (!intent.getStringExtra("emp").contains(child.child("name").getValue().toString()) &&
                                            !intent.getStringExtra("ids").contains(child.child("id").getValue().toString())
                                    ) {
                                        isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(), false, "");
                                        dataAdapter.add(isi);
                                    }
                                }
                            }

                            else if(intent.getStringExtra("empname")!= null && intent.getStringExtra("supervisor")!= null){
                                if (intent.getStringExtra("empname") != null || intent.getStringExtra("empname") != "") {
Firebaselinks.assignsvtoemp().orderByChild("Supervisor").equalTo(intent.getStringExtra("empname")).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
    if(dataSnapshot.getChildrenCount() > 0){
        for(DataSnapshot child : dataSnapshot.getChildren()){
            if(dataAdapter.size() > 0)
                dataAdapter.clear();

            if(child.child("Employee").getValue().toString().contains(",")){
String[] h = child.child("Employee").getValue().toString().split(",");
String[] h1 = child.child("Employeeids").getValue().toString().split(",");
for(int i=0;i<h.length;i++){
    isi = new DataAdapter(h[i],h1[i], false, "");
    dataAdapter.add(isi);
}
            }
            else{
                isi = new DataAdapter(child.child("Employee").getValue().toString(), child.child("Employeeids").getValue().toString(), false, "");
                dataAdapter.add(isi);
            }
        }
        Validations.setRecyclerviewLayoutManager(recyclerView, getApplicationContext());
        adapter = new CustomAdapter(getApplicationContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);
    }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
});
                                }
                            }




                            else {
                                isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(), false, "");
                                dataAdapter.add(isi);
                            }
                        }
                    }
                    Validations.setRecyclerviewLayoutManager(recyclerView, getApplicationContext());
                    adapter = new CustomAdapter(getApplicationContext(), dataAdapter, listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }





    public void save_clicked(View view) {

        if(!txtselecteditems.getText().toString().isEmpty()) {
            Intent returnInt = new Intent();
            List<String> nameslist = new ArrayList<>();
            List<String> idslist = new ArrayList<>();
            List<String> desclist = new ArrayList<>();
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                if (recyclerView.findViewHolderForAdapterPosition(i) instanceof CustomAdapter.MyHolder) {

                    CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForAdapterPosition(i);

                    if (holder.imgcheck.getVisibility() == View.VISIBLE) {
                        nameslist.add(holder.txt.getText().toString());
                        idslist.add(holder.id.getText().toString());
                    }


                }
            }

            returnInt.putExtra("result", nameslist.toString().replace("[", "").replace("]", ""));
            returnInt.putExtra("resultids", idslist.toString().replace("[", "").replace("]", ""));
            //   returnInt.putExtra("resultdesc",desclist.toString().replace("[", "").replace("]", ""));
            setResult(RESULT_OK, returnInt);
            finish();
        }
        else
            Validations.Toast(getApplicationContext(),"Please Select Employee");
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        int positionClicked;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;
        List<String> newtotal =new ArrayList<String>() ;
        Context context;
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.selectemprowdata, parent, false);
            return new CustomAdapter.MyHolder(v, listener);
        }

        public CustomAdapter(Context context, List<DataAdapter> data, RecyclerViewClickListener listener) {
            this.context = context;
            this.data = data;
            data1 = new ArrayList<>(data);
            this.listener = listener;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onBindViewHolder(CustomAdapter.MyHolder holder, int position) {
            holder.txt.setText(data.get(position).getName());


            if (data.get(position).isClicked()){
                data.get(position).setClicked(false);
                holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                data.get(position).setClicked(true);
             //   holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }

            if(newtotal.size() > 0){


                if(newtotal.contains(data.get(position).getText())){
                    {
                        holder.imgcheck.setVisibility(View.VISIBLE);

                        holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                }

            }

            holder.txt.setText(data.get(position).getText());
            holder.id.setText(data.get(position).getId());



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



                            if(!item.getSenddesctoemp().toLowerCase().isEmpty())
                                item.setSenddesctoemp(item.getSenddesctoemp().toString());
                            else
                                item.setSenddesctoemp("");

                                    if(!item.getChk()) {
                                        item.setChk(false);

                                    }
                                    else {
                                        item.setChk(true);
                                    }

                                    ischecked = true;
                                      filteredList.add(item);

                                }
                        }
                        ischecked = true;
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

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView txt,id,role;
            ImageView imgcheck;
            CardView cardView;
            ImageButton clearbtn;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                role = itemView.findViewById(R.id.role);
                id = itemView.findViewById(R.id.id);
                cardView = itemView.findViewById(R.id.cardview);
                imgcheck = itemView.findViewById(R.id.imageview);
role.setVisibility(View.GONE);
                clearbtn = itemView.findViewById(R.id.clearbtn);
                clearbtn.setVisibility(View.GONE);
                this.mListener=mListener;
                itemView.setOnClickListener(this);


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recyclerView.findViewHolderForLayoutPosition(getAdapterPosition()) instanceof CustomAdapter.MyHolder) {

                            CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(getAdapterPosition());
                            positionClicked = getAdapterPosition();

                            if (holder.imgcheck.getVisibility() == View.GONE) {
                                holder.imgcheck.setVisibility(View.VISIBLE);
                                holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                newtotal.add(dataAdapter.get(getAdapterPosition()).getText().toString());
                                txtselecteditems.setText(newtotal.toString().replace("[", "").replace("]", ""));
                            }
                            else {
                                holder.imgcheck.setVisibility(View.GONE);
                                holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.blackColor));
                                newtotal.remove(dataAdapter.get(getAdapterPosition()).getText().toString());
                                txtselecteditems.setText(newtotal.toString().replace("[", "").replace("]", ""));

                            }
                        }
                    }
                });


                final List<String>[] finalarray = new List[]{new ArrayList<>()};
//                senddesctoemp.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                        if(checkBox.isChecked()) {
//                            temp = s.toString().trim() + "";
//                        }
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (checkBox.isChecked()) {
//                            for (int j = 0; j < newtotal.size(); j++) {
//                               abc.add(newtotal.get(j).trim().isEmpty() ? "" :   newtotal.get(j).trim());
//                            }
//
//                            abc.remove(temp.trim().isEmpty() ? 0f : temp.trim());
//                            newtotal.remove(temp.trim().toString());
//                            newtotal.add(s.toString().trim()+"");
//
//
//
//
//
//                            finalarray[0] = newtotal;
//
//txtselecteddesc.setText(finalarray[0].toString().replace("[", "").replace("]", ""));
//
//                        }
//                    }
//                    @Override
//                    public void afterTextChanged(Editable s) {
////                        String str = senddesctoemp.getText().toString();
////                        if(str.length() > 0)
////                        {try {
////                            senddesctoemp.setText(senddesctoemp.getText().toString().replaceAll(" ", ""));
////                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                                senddesctoemp.setFocusable(senddesctoemp.getText().length());
////                            }
////                        }catch(Exception e){}
////                        }
//                    }
//                });


//
//llt1.setOnLongClickListener(new View.OnLongClickListener() {
//    @Override
//    public boolean onLongClick(View v) {
//
//        checkBox.setVisibility(View.VISIBLE);
//        checkBox.setChecked(true);
//
//        return false;
//    }
//});


            }

            @Override
            public void onClick(View v) {
                if(mListener!= null)
                mListener.onClick(v, getAdapterPosition());
            }
        }

    }

    public  class DataAdapter {

        String text,id,senddesctoemp;
        LinearLayout llt;
Boolean chk = false;
        private String name;
        private boolean clicked;
        public LinearLayout getLlt() {
            return llt;
        }

        public void setLlt(LinearLayout llt) {
            this.llt = llt;
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

        public String getSenddesctoemp() {
            return senddesctoemp;
        }

        public void setSenddesctoemp(String senddesctoemp) {
            this.senddesctoemp = senddesctoemp;
        }

        public void setText(String text) {
            this.text = text;
        }

        public DataAdapter(String text,String id,Boolean chk,String senddesctoemp) {
            this.text =text;
            this.id=id;
            this.chk =chk;
            this.senddesctoemp =senddesctoemp;
        }

        public DataAdapter(String text,String id,Boolean chk) {
            this.text =text;
            this.id=id;
            this.chk =chk;
        }
        // setters and getters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }
        public Boolean getChk() {
            return chk;
        }

        public void setChk(Boolean chk) {
            this.chk = chk;
        }
    }
}
