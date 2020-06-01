package com.example.timesheetapplication.Admin.java;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
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
import java.util.Objects;


public class ProjectList extends Fragment {
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    View view;
    public String type;
AppCompatEditText txtsearch;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Projects";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
view  = inflater.inflate(R.layout.fragment_project_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        txtsearch = view.findViewById(R.id.searchtxt);
        dataAdapter = new ArrayList<>();

        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                AddnewProject fragment = new AddnewProject();
                Bundle bundle = new Bundle();
                bundle.putString("projname", dataAdapter.get(position).getText().toString());
                bundle.putString("projid", dataAdapter.get(position).getId().toString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame,fragment);
                fragmentTransaction.commit();
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
        Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
        adapter = new CustomAdapter(getContext(), dataAdapter, listener);
        recyclerView.setAdapter(adapter);

        return view;
    }


    public void DataLoad(){

        Firebaselinks.projectsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                        if (childsnapshot.child("projstatus").getValue().toString().equals("Active")) {
                                isi = new DataAdapter(R.drawable.ic_circle,childsnapshot.child("projname").getValue().toString(), childsnapshot.child("projid").getValue().toString());
                                dataAdapter.add(isi);
                            } else {
                                isi = new DataAdapter(R.drawable.ic_inactive,childsnapshot.child("projname").getValue().toString(), childsnapshot.child("projid").getValue().toString());
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
        public void onBindViewHolder(final CustomAdapter.MyHolder holder, int position) {
            holder.txt.setText(data.get(position).getText());
            holder.id.setText(data.get(position).getId());
         holder.imageView.setBackgroundResource(data.get(position).getDrawable());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txt,id,desc;
            ImageView imageView;
           CardView cardView;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                id = itemView.findViewById(R.id.id);
                desc = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.imageview);
            cardView = itemView.findViewById(R.id.cardview);
            desc.setVisibility(View.GONE);
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

        String text,id;
        int drawable;

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
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


        public DataAdapter(int drawable, String text, String id) {
            this.text =text;
            this.id=id;
            this.drawable =drawable;
        }



        public DataAdapter(String text, String id) {
            this.text =text;
            this.id=id;
        }
    }
}
