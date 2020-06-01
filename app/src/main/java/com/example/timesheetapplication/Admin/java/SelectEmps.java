package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.RecyclerViewClickListener;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectEmps extends AppCompatActivity {
    AppCompatEditText txtselecteditems,txtselectedids,txtselecteddesc;
    RecyclerView recyclerView;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    AppCompatEditText txtsearch;
    Button savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_emps);
        txtselecteditems = findViewById(R.id.selecteditems);
        txtselectedids = findViewById(R.id.selectedids);
        txtselecteddesc = findViewById(R.id.selecteddesc);
        recyclerView = findViewById(R.id.recyclerview);
        dataAdapter = new ArrayList<>();
        txtsearch = findViewById(R.id.searchtxt);
        savebtn = findViewById(R.id.Savebtn);
        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {



            }
            };
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

savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!txtselecteditems.getText().toString().isEmpty()) {
            Intent returnInt = new Intent();
            List<String> nameslist = new ArrayList<>();
            List<String> idslist = new ArrayList<>();
            List<String> desclist = new ArrayList<>();
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                if (recyclerView.findViewHolderForLayoutPosition(i) instanceof CustomAdapter.MyHolder) {

                    CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(i);

                    if (holder.imgcheck.getVisibility() == View.VISIBLE) {
                        nameslist.add(holder.txt.getText().toString());
                        idslist.add(holder.id.getText().toString());
//                    if(!holder.senddesctoemp.getText().toString().isEmpty()){
//
//
//                        if(holder.senddesctoemp.getText().toString().contains(","))
//                            holder.senddesctoemp.setText(holder.senddesctoemp.getText().toString().replace(',', ' '));
//
//                        desclist.add(holder.senddesctoemp.getText().toString());
//
//                    }
//                    else {
//                        desclist.add("");
//                    }
                    }


                }
            }

            returnInt.putExtra("result", nameslist.toString().replace("[", "").replace("]", ""));
            returnInt.putExtra("resultids", idslist.toString().replace("[", "").replace("]", ""));
            //    returnInt.putExtra("resultdesc",desclist.toString().replace("[", "").replace("]", ""));
            setResult(RESULT_OK, returnInt);
            finish();
        }
        else
        Validations.Toast(getApplicationContext(),"Please Select Employee");
    }
});





        Validations.setRecyclerviewLayoutManager(recyclerView, getApplicationContext());
        adapter = new CustomAdapter(getApplicationContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);

    }

    public void DataLoad(){
final List<String> emplist = new ArrayList<>();
        Intent intent = getIntent();
        if(intent!= null) {

           if(intent.getStringExtra("svname") != null) {

               Firebaselinks.assignsvtoemp().orderByChild("Supervisor").equalTo(intent.getStringExtra("svname")).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.getChildrenCount() > 0) {
                           for (DataSnapshot child : dataSnapshot.getChildren()) {
                               emplist.add(child.child("Employee").getValue().toString());
                           }


                           dataAdapter = new ArrayList<>();
                           Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.getChildrenCount() > 0) {
                                       for (DataSnapshot child : dataSnapshot.getChildren()) {
                                           if (!emplist.get(0).contains(child.child("name").getValue().toString())) {
                                               if (child.child("status").getValue().toString().equals("Active")) {
                                                   isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(), child.child("profile").getValue().toString(), false);
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
                       else{
                           dataAdapter = new ArrayList<>();
                           Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.getChildrenCount() > 0) {
                                       for (DataSnapshot child : dataSnapshot.getChildren()) {
                                           if (child.child("status").getValue().toString().equals("Active")) {
                                               isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(),child.child("profile").getValue().toString() , false);
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

                   @Override
                   public void onCancelled(FirebaseError firebaseError) {

                   }
               });


           }
           if(intent.getStringExtra("projid") !=null){
               Firebaselinks.assignedprojectsfirebaselink().orderByChild("projid").equalTo(intent.getStringExtra("projid")).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.getChildrenCount() > 0) {
                           for (DataSnapshot child : dataSnapshot.getChildren()) {
emplist.add(child.child("assignedids").getValue().toString());
                           }


                           dataAdapter = new ArrayList<>();
                           Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.getChildrenCount() > 0) {
                                       for (DataSnapshot child : dataSnapshot.getChildren()) {
                                           if (!emplist.get(0).contains(child.child("id").getValue().toString())) {
                                               if (child.child("status").getValue().toString().equals("Active")) {
                                                   isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(), child.child("profile").getValue().toString(), false);
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
                       else{
                           dataAdapter = new ArrayList<>();
                           Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.getChildrenCount() > 0) {
                                       for (DataSnapshot child : dataSnapshot.getChildren()) {
                                           if (child.child("status").getValue().toString().equals("Active")) {
                                               isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(),child.child("profile").getValue().toString() , false);
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

                   @Override
                   public void onCancelled(FirebaseError firebaseError) {

                   }
               });

           }



            if(intent.getStringExtra("admin") != null) {

                Firebaselinks.adminfirebaselink().orderByChild("admin").equalTo(intent.getStringExtra("admin")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                emplist.add(child.child("people").getValue().toString());
                            }


                            dataAdapter = new ArrayList<>();
                            Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            if (!emplist.get(0).contains(child.child("name").getValue().toString())) {
                                                if (child.child("status").getValue().toString().equals("Active")) {
                                                    isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(), child.child("profile").getValue().toString(), false);
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
                        else{
                            dataAdapter = new ArrayList<>();
                            Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            if (child.child("status").getValue().toString().equals("Active")) {
                                                isi = new DataAdapter(child.child("name").getValue().toString(), child.child("id").getValue().toString(),child.child("profile").getValue().toString() , false);
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

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }


        }


    }


//    public void abc(int position) {
//        newtotal.add(dataAdapter.get(position).getText().toString());
////        dataAdapter.remove(position);
////        Validations.setRecyclerviewLayoutManager(recyclerView, getApplicationContext());
////        adapter = new CustomAdapter(getApplicationContext(), dataAdapter, listener);
////        recyclerView.setAdapter(adapter);
//
//        txtselecteditems.setText(newtotal.toString().replace("[", "").replace("]", ""));
//    }
//
//
//    public void abc1(int position) {
//
//    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        int positionClicked;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;
        List<String> newtotal =new ArrayList<String>() ;
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

        public void onBindViewHolder(final CustomAdapter.MyHolder holder, int position) {


            holder.txt.setText(data.get(position).getName());


            if (data.get(position).isClicked()){
                data.get(position).setClicked(false);
                holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                data.get(position).setClicked(true);
              // holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
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
            holder.role.setText(data.get(position).getRole());


        }




        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageButton clearbtn;
            TextView txt,id,role;
            ImageView imgcheck;
            CardView cardView;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                id = itemView.findViewById(R.id.id);
                role = itemView.findViewById(R.id.role);
                cardView = itemView.findViewById(R.id.cardview);
                imgcheck = itemView.findViewById(R.id.imageview);
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
                int size = newtotal.size();
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

            }

            @Override
            public void onClick(View v) {
                if(mListener!= null) {
                    mListener.onClick(v, getAdapterPosition());

                }
            }

        }



    }







    public  class DataAdapter {
        private String name;
        private boolean clicked;

        String text,id,role;
        Boolean chk;


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


        public DataAdapter(String text,String id,String role,Boolean chk) {
            this.text =text;
            this.id=id;
            this.role= role;
        }


//
//
//        public DataAdapter(String text,String id,Boolean chk) {
//            this.text =text;
//            this.id=id;
//            this.chk =chk;
//        }

        public Boolean getChk() {
            return chk;
        }

        public void setChk(Boolean chk) {
            this.chk = chk;
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
    }
}
