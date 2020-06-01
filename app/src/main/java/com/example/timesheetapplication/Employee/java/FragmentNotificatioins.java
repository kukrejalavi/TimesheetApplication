package com.example.timesheetapplication.Employee.java;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class FragmentNotificatioins extends Fragment {
    RecyclerView recyclerView;
TextView txtnomsg;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    Button savebtn;
    TextView txttotalhrs;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
View view;
    public     String empid,empname,type;
    public int  notifycount;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Inbox";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_notificatioins, container, false);
        recyclerView= view.findViewById(R.id.recyclerview);
        txtnomsg= view.findViewById(R.id.nomsgtxt);
        dataAdapter = new ArrayList<>();


        Firebaselinks.notificationsfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(final DataSnapshot child : dataSnapshot.getChildren()){

                     Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             if(dataSnapshot.getChildrenCount() > 0){
                                 for(DataSnapshot accchild : dataSnapshot.getChildren()) {
                                     if (accchild.child("id").getValue().toString().equals(child.child("sentby").getValue().toString())){
                                         if (child.child("empid").getValue().toString().contains(",")) {
                                             String h = child.child("empid").getValue().toString();

                                             String[] f = h.split(",");
                                             for (int i = 0; i < f.length; i++) {
                                                 if (f[i].contains(" ")) {
                                                     f[i] = f[i].replace(" ", "");
                                                 }
                                                 if (f[i].equals(empid)) {
                                                     isi = new DataAdapter(Validations.dashtoslash(Validations.newyyyytoddwithouttime(child.child("date").getValue().toString())), child.child("title").getValue().toString(), child.child("message").getValue().toString(), accchild.child("profile").getValue().toString());
                                                     dataAdapter.add(isi);
                                                 }
                                             }
                                         } else {
                                             if (child.child("empid").getValue().toString().equals(empid)) {
                                                 isi = new DataAdapter(Validations.dashtoslash(Validations.newyyyytoddwithouttime(child.child("date").getValue().toString())), child.child("title").getValue().toString(), child.child("message").getValue().toString(), accchild.child("profile").getValue().toString());
                                                 dataAdapter.add(isi);
                                             }
                                         }
                                 }
                                 }
                                 Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                                 adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                                 recyclerView.setAdapter(adapter);

                                 if(dataAdapter.size() > 0)
                                 {
                                     txtnomsg.setVisibility(View.GONE);
                                 }

                             }

                         }

                         @Override
                         public void onCancelled(FirebaseError firebaseError) {

                         }
                     });
                    }
                    if(dataAdapter.size() == 0)
                    {
                        txtnomsg.setVisibility(View.VISIBLE);
                    }


                }
                else{
                    if (dataAdapter.size() == 0) {
                        txtnomsg.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        return view;
    }



    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        Context context;
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.notificationsrowdata, parent, false);
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
                        if (!item.getTitle().toLowerCase().equals(null)) {
                            if (item.getTitle().toLowerCase().contains(filterPattern)) {
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
            holder.txttitle.setText(data.get(position).getTitle());
            holder.txtmsg.setText(data.get(position).getMsg());
            holder.txtreceivedfrom.setText(data.get(position).getReceivedfrom());
         holder.txtdate.setText(data.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txttitle, txtmsg,txtdate,txtreceivedfrom,txttype;
            RecyclerViewClickListener mListener;
ImageView imageview;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txttype = itemView.findViewById(R.id.type);
                txttitle = itemView.findViewById(R.id.title);
                txtdate = itemView.findViewById(R.id.date);
                txtmsg = itemView.findViewById(R.id.msg);
                txtreceivedfrom = itemView.findViewById(R.id.receivedfrom);
                imageview = itemView.findViewById(R.id.image);
                this.mListener = mListener;
                itemView.setOnClickListener(this);

                txttype.setText("Received from:");
            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
//                    mListener.onClick(v, getAdapterPosition());
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

        String title,msg,date,receivedfrom;

        public String getReceivedfrom() {
            return receivedfrom;
        }

        public void setReceivedfrom(String receivedfrom) {
            this.receivedfrom = receivedfrom;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


        public DataAdapter(String date,String title,String msg,String receivedfrom){
this.date = date;
this.title = title;
this.msg = msg;
this.receivedfrom = receivedfrom;
        }
    }
}
