package com.example.timesheetapplication;

import android.content.Context;
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
public class Sentmsg extends Fragment {
View view;
    RecyclerView recyclerView;
    TextView txtnomsg;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    public     String empid,empname,type;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Sent Messages";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sent_message, container, false);
        recyclerView= view.findViewById(R.id.recyclerview);
        txtnomsg= view.findViewById(R.id.nomsgtxt);
        dataAdapter = new ArrayList<>();



        Firebaselinks.notificationsfirebaselink().orderByChild("sentby").equalTo(empid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    final List<String> empnames = new ArrayList<>();
                    final List<String> finalnames = new ArrayList<>();
for(final DataSnapshot notifychild : dataSnapshot.getChildren()) {
    Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getChildrenCount() > 0) {
                if(empnames.size() > 0)
                    empnames.clear();
                if(finalnames.size() > 0)
                    finalnames.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                empnames.add(child.child("id").getValue().toString() + "_:" + child.child("name").getValue().toString());
                }
                    if (notifychild.child("empid").getValue().toString().contains(",")) {
                        String h = notifychild.child("empid").getValue().toString();

                        String[] f = h.split(",");
                        for (int i = 0; i < f.length; i++) {
                            if (f[i].contains(" ")) {
                                f[i] = f[i].replace(" ", "");
                            }
                            if (empnames.get(i).split("_:")[0].contains(f[i])) {
finalnames.add(empnames.get(i).split("_:")[1]);
                            }
                        }
                        isi = new DataAdapter(Validations.dashtoslash(Validations.newyyyytoddwithouttime(notifychild.child("date").getValue().toString())), notifychild.child("title").getValue().toString(), notifychild.child("message").getValue().toString(), finalnames.toString().replace("[", "").replace("]", ""));
                        dataAdapter.add(isi);
                    }
                    else{
                        if(finalnames.size() > 0)
                            finalnames.clear();


                        for(int i=0;i<empnames.size();i++) {
                            if (empnames.get(i).split("_:")[0].equals(notifychild.child("empid").getValue().toString())) {
                                finalnames.add(empnames.get(i).split("_:")[1]);
                            }
                        }

                            isi = new DataAdapter(Validations.dashtoslash(Validations.newyyyytoddwithouttime(notifychild.child("date").getValue().toString())),notifychild.child("title").getValue().toString(),notifychild.child("message").getValue().toString(), finalnames.toString().replace("[", "").replace("]", ""));
                            dataAdapter.add(isi);

                    }


                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                recyclerView.setAdapter(adapter);
                if(dataAdapter.size() == 0)
                {
                    txtnomsg.setVisibility(View.VISIBLE);
                }


            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });

}

                }
else {
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
            holder.txtsentto.setText(data.get(position).getSentto());
            holder.txtdate.setText(data.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txttitle, txtmsg,txtdate,txtsentto,txttype;
            RecyclerViewClickListener mListener;
            ImageView imageview;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txttype = itemView.findViewById(R.id.type);
                txttitle = itemView.findViewById(R.id.title);
                txtdate = itemView.findViewById(R.id.date);
                txtmsg = itemView.findViewById(R.id.msg);
                txtsentto = itemView.findViewById(R.id.receivedfrom);
                imageview = itemView.findViewById(R.id.image);
                this.mListener = mListener;
                itemView.setOnClickListener(this);

                txttype.setText("Sent to:");

            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
//
                }
            }
        }

    }
    public class DataAdapter {

        String title,msg,date,sentto;

        public String getSentto() {
            return sentto;
        }

        public void setSentto(String sentto) {
            this.sentto = sentto;
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


        public DataAdapter(String date,String title,String msg,String sentto){
            this.date = date;
            this.title = title;
            this.msg = msg;
            this.sentto = sentto;
        }
    }
}
