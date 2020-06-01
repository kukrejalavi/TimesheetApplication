package com.example.timesheetapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class CalendarView extends Fragment {
    CompactCalendarView compactCalendar;
    ImageButton imageButton;
    RecyclerViewClickListener listener;
    DataAdapter isi;
    ArrayList<DataAdapter> dataAdapter;
    CustomAdapter adapter;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    String newdate="";
    RecyclerView recyclerView;
View view;
    public String empid,empname,logintype;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view =  inflater.inflate(R.layout.fragment_calendar_view, container, false);


        dataAdapter = new ArrayList<>();
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);



String month_year = SetDate.getMonth() + "-" + SetDate.getyear();

        actionBar.setTitle(month_year);

        imageButton = view.findViewById(R.id.btn);
        recyclerView = view.findViewById(R.id.recyclerview);

        compactCalendar = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);

        compactCalendar.setUseThreeLetterAbbreviation(true);


      if(logintype == null || logintype.equals(""))
          imageButton.setVisibility(View.GONE);

        //String strDate = convert(newdate);


        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (logintype != null && logintype.equals("Admin")) {
                    String strDate = convert(newdate);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    AddCalendarEvent fragment = new AddCalendarEvent();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", strDate);
                    bundle.putString("note", dataAdapter.get(position).getText());
                    bundle.putString("eventid", dataAdapter.get(position).getEventid());
                    bundle.putString("typeeditdelete", "typeeditdelete");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();

                    fragmentTransaction.addToBackStack(null);
                    if (logintype != null) {
                        if (logintype.equals("Admin")) {
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            fragment.logintype = "Admin";
                            fragment.empid = empid;
                            fragment.empname = empname;
                        }
                    } else {
                        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                        fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragment).commit();
                    }
                    fragmentTransaction.commit();

                }
            }
        };

        Firebaselinks.calendarfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()) {

                        String myDate =Validations.dashtoslash(Validations.yyyytoddwithouttime(Validations.dashtoslash(child.child("eventdate").getValue().toString())));


                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long timeInMillis = date.getTime();

                        Event ev1 = new Event(Color.RED, timeInMillis, myDate);

                        compactCalendar.addEvent(ev1);

                        if(myDate.equals(SetDate.Currentdatewithmonthwithout0())){
                                isi = new DataAdapter(child.child("note").getValue().toString(), child.child("eventid").getValue().toString());
                                dataAdapter.add(isi);
                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdate != null && !newdate.equals("")) {
                    String strDate = convert(newdate);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    AddCalendarEvent fragment = new AddCalendarEvent();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", strDate);
                    bundle.putString("typeadd", "typeadd");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentTransaction.addToBackStack(null);
                    if (logintype != null) {
                        if (logintype.equals("Admin")) {
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            fragment.logintype = "Admin";
                            fragment.empid = empid;
                            fragment.empname = empname;
                        }
                    } else {
                        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                        fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragment).commit();
                    }
                    fragmentTransaction.commit();

                }
            }
        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override

            public void onDayClick(final Date dateClicked) {
                newdate  = dateClicked.toString();

                Firebaselinks.calendarfirebaselink().orderByChild("eventdate").equalTo(converttoyyyy(newdate)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
                            if(dataAdapter.size() > 0)
                                dataAdapter.clear();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                isi = new DataAdapter(child.child("note").getValue().toString(),child.child("eventid").getValue().toString()
                                );
                                dataAdapter.add(isi);
                            }

                            Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                            adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                            recyclerView.setAdapter(adapter);
                        }
                        else{
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



            @Override

            public void onMonthScroll(Date firstDayOfNewMonth) {

                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
dataAdapter.clear();
                Validations.setRecyclerviewLayoutManager(recyclerView, getContext());
                adapter = new CustomAdapter(getContext(), dataAdapter, listener);
                recyclerView.setAdapter(adapter);


            }

        });


        return view;
    }


    public String convert(String newdate){
        String strMnth    = newdate.substring(4,7);
        String day        = newdate.substring(8,10);
        String year       = newdate.substring(30,34);
        String strMonth = "" ;
        if(strMnth.equals("Jan"))
            strMonth ="1";
        else if(strMnth.equals("Feb"))
            strMonth="2";
        else if(strMnth.equals("Mar"))
            strMonth="3";
        else if(strMnth.equals("Apr"))
            strMonth="4";
        else if(strMnth.equals("May"))
            strMonth="5";
        else if(strMnth.equals("Jun"))
            strMonth="6";
        else if(strMnth.equals("Jul"))
            strMonth="7";
        else if(strMnth.equals("Aug"))
            strMonth="8";
        else if(strMnth.equals("Sep"))
            strMonth="9";
        else if(strMnth.equals("Oct"))
            strMonth="10";
        else if(strMnth.equals("Nov"))
            strMonth="11";
        else if (strMnth.equals("Dec"))
            strMonth ="12";

        String strDate =  day + '/' + strMonth  +'/'+year;
        return strDate;
    }

    public String converttoyyyy(String newdate){
        String strMnth    = newdate.substring(4,7);
        String day        = newdate.substring(8,10);
        String year       = newdate.substring(30,34);
        String strMonth = "" ;
        if(strMnth.equals("Jan"))
            strMonth ="1";
        else if(strMnth.equals("Feb"))
            strMonth="2";
        else if(strMnth.equals("Mar"))
            strMonth="3";
        else if(strMnth.equals("Apr"))
            strMonth="4";
        else if(strMnth.equals("May"))
            strMonth="5";
        else if(strMnth.equals("Jun"))
            strMonth="6";
        else if(strMnth.equals("Jul"))
            strMonth="7";
        else if(strMnth.equals("Aug"))
            strMonth="8";
        else if(strMnth.equals("Sep"))
            strMonth="9";
        else if(strMnth.equals("Oct"))
            strMonth="10";
        else if(strMnth.equals("Nov"))
            strMonth="11";
        else if (strMnth.equals("Dec"))
            strMonth ="12";

        String strDate =  year + '-' + strMonth  +'-' + day;
        return strDate;
    }



    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        RecyclerViewClickListener listener;
        List<DataAdapter> data;
        List<DataAdapter> data1;
        List<String> newtotal =new ArrayList<String>() ;
        Context context;
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.singletextviewrowdata, parent, false);
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
            holder.txt.setText(data.get(position).getText());
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
            TextView txt,eventid,role;
            ImageView imgcheck;
            CardView cardView;
            ImageButton clearbtn;
            RecyclerViewClickListener mListener;
            public MyHolder(View itemView, RecyclerViewClickListener mListener) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                eventid = itemView.findViewById(R.id.eventid);
//                role = itemView.findViewById(R.id.role);
//                id = itemView.findViewById(R.id.id);
//                cardView = itemView.findViewById(R.id.cardview);
//                imgcheck = itemView.findViewById(R.id.imageview);
//                role.setVisibility(View.GONE);
//                clearbtn = itemView.findViewById(R.id.clearbtn);
//                clearbtn.setVisibility(View.GONE);
                this.mListener=mListener;
                itemView.setOnClickListener(this);


//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (recyclerView.findViewHolderForLayoutPosition(getAdapterPosition()) instanceof CustomAdapter.MyHolder) {
//
//                            CustomAdapter.MyHolder holder = (CustomAdapter.MyHolder) recyclerView.findViewHolderForLayoutPosition(getAdapterPosition());
//                            positionClicked = getAdapterPosition();
//
//                            if (holder.imgcheck.getVisibility() == View.GONE) {
//                                holder.imgcheck.setVisibility(View.VISIBLE);
//                                holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//                                newtotal.add(dataAdapter.get(getAdapterPosition()).getText().toString());
//                                txtselecteditems.setText(newtotal.toString().replace("[", "").replace("]", ""));
//                            }
//                            else {
//                                holder.imgcheck.setVisibility(View.GONE);
//                                holder.imgcheck.setBackgroundColor(context.getResources().getColor(R.color.blackColor));
//                                newtotal.remove(dataAdapter.get(getAdapterPosition()).getText().toString());
//                                txtselecteditems.setText(newtotal.toString().replace("[", "").replace("]", ""));
//
//                            }
//                        }
//                    }
//                });


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

        String text,eventid;

        public String getEventid() {
            return eventid;
        }

        public void setEventid(String eventid) {
            this.eventid = eventid;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


        public DataAdapter(String text,String eventid) {
            this.text =text;
            this.eventid=eventid;

        }


    }
}
