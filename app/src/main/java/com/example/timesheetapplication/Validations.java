package com.example.timesheetapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.AnimRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Validations {

public static String currentDate(){
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String DateTime = df.format(new Date());
    return DateTime;
}

public static String ddtoyyyywithtime(String dd){

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    String CurrentTime = df.format(new Date());
    String date="";
    if(dd.contains("/")){
    String yyyymmdd = dd.split("/")[2] + "-" + dd.split("/")[1] + "-" + dd.split("/")[0];
     date = yyyymmdd + " " + CurrentTime;}
    if(dd.contains("-")){
        String yyyymmdd = dd.split("-")[2] + "-" + dd.split("-")[1] + "-" + dd.split("-")[0];
         date = yyyymmdd + " " + CurrentTime;
    }
    return  date;
}

    public static String ddtoyyyywithouttime(String dd){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
     //   String CurrentTime = df.format(new Date());
        String date="";
        if(dd.contains("/")){
            String yyyymmdd = dd.split("/")[2] + "-" + dd.split("/")[1] + "-" + dd.split("/")[0];
            date = yyyymmdd ;}
        if(dd.contains("-")){
            String yyyymmdd = dd.split("-")[2] + "-" + dd.split("-")[1] + "-" + dd.split("-")[0];
            date = yyyymmdd ;
        }
        return  date;
    }

    public static String yyyytoddwithtime(String yyyy){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String CurrentTime = df.format(new Date());

        String date="";
        if(yyyy.contains("/")){
            String ddmmyyyy = yyyy.split("/")[2] + "-" + yyyy.split("/")[1] + "-" + yyyy.split("/")[0];
            date = ddmmyyyy + " " + CurrentTime;}
        if(yyyy.contains("-")){
            String ddmmyyyy = yyyy.split("-")[2] + "-" + yyyy.split("-")[1] + "-" + yyyy.split("-")[0];
            date = ddmmyyyy + " " + CurrentTime;
        }
        return  date;
    }

    public static String yyyytoddwithouttime(String yyyy){

      //  SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
       // String CurrentTime = df.format(new Date());

        String ddmmyyyy="";
        if(yyyy.contains("/")) {
             ddmmyyyy = yyyy.split("/")[2] + "-" + yyyy.split("/")[1] + "-" + yyyy.split("/")[0];
            //  date = ddmmyyyy + " " + CurrentTime;
        }
        if(yyyy.contains("-")){
             ddmmyyyy = yyyy.split("-")[2] + "-" + yyyy.split("-")[1] + "-" + yyyy.split("-")[0];
            //date = ddmmyyyy + " " + CurrentTime;
        }
        return  ddmmyyyy;
    }

    public static String newyyyytoddwithouttime(String yyyywithtime){
        String ddmmyyyy="";
        if(yyyywithtime.contains("/")) {
            ddmmyyyy = yyyywithtime.split("/")[2] + "-" + yyyywithtime.split("/")[1] + "-" + yyyywithtime.split("/")[0];
        }
        if(yyyywithtime.contains("-")){
            String[] a = yyyywithtime.split(" ");
            ddmmyyyy = a[0].split("-")[2] + "-" + a[0].split("-")[1] + "-" + a[0].split("-")[0];
        }
        return  ddmmyyyy;
    }

    public static String dashtoslash(String dashdatewithouttime){
    String slashdate = dashdatewithouttime.replace('-','/');

    return  slashdate;

    }

    public static String slashtodash(String slashdatewithouttime){
        String dashdate = slashdatewithouttime.replace('/','-');

        return  dashdate;

    }


    public static Boolean IsEmpty(TextView text) {
        if (text.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }



    public static Boolean IsEmpty(EditText text) {
        if (text.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public static Boolean MultipleIsEmpty(EditText... text) {
        for(TextView a: text) {
            if (a.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static Boolean checkforand(TextView... abc){
for(TextView a: abc){
if(a.getText().toString().isEmpty()){
    return false;
}

}

return true;

    }



    public static Boolean checkforand(EditText... abc){
        for(EditText a: abc){
            if(a.getText().toString().isEmpty()){
                return false;
            }

        }

        return true;

    }


    public static void Toast(Context ctx,String ex){
        Toast.makeText(ctx,ex+"",Toast.LENGTH_LONG).show();
    }


public static String execsql(SQLiteDatabase db, String tablename,String... abc){

        for(int i=0;i<abc.length ; i++) {
            String h=abc[i];
            String[] columnvalues= h.split(";");
            db.execSQL("insert into " + tablename + "(" + columnvalues[0]  +")" +  " values('" + columnvalues[1] + "')");
        }
        return "";
}

    public static Boolean ClearData(EditText... abc){
        for(EditText a: abc){
            if(!a.getHint().equals("Date")) {
                if (!a.getText().toString().isEmpty()) {
                    a.setText("");
                }
            }
            else
            {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                String currentdate=simpleDateFormat.format(new Date());
                a.setText(currentdate);
            }
        }

        return true;

    }


    public static Boolean ClearData(TextView... abc){
        for(TextView a: abc){
            if(!a.getHint().equals("Date")) {
                if (!a.getText().toString().isEmpty()) {
                    a.setText("");
                }
            }
            else
            {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                String currentdate=simpleDateFormat.format(new Date());
                a.setText(currentdate);
            }
        }

        return true;

    }

public static String gettexttostring(EditText text){
    String abc="";
    if(!text.toString().isEmpty())
        abc = text.getText().toString();
    return abc;
}

    public static Boolean isempty(EditText text){
        if(!text.toString().isEmpty())
            return true;
        else
             return false;
    }


    public static String gettexttostring(Spinner text){
        String abc="";
        if(!text.toString().isEmpty())
            abc = text.getSelectedItem().toString();
        return abc;
    }

    public static String gettexttostring(TextView text){
        String abc="";
        if(!text.toString().isEmpty())
            abc = text.getText().toString();
        return abc;
    }

    public static Boolean isempty(TextView text){
        if(!text.toString().isEmpty())
            return true;
        else
            return false;
    }





    public static Boolean ClearDataWithoutHint(TextView... abc){
        for(TextView a: abc){
                if (!a.getText().toString().isEmpty()) {
                    a.setText("");
                }
        }
        return true;
    }

    public static Boolean ClearDataWithoutHint(EditText... abc){
        for(EditText a: abc){
            if (!a.getText().toString().isEmpty()) {
                a.setText("");
            }
        }
        return true;
    }

    public static void setRecyclerviewLayoutManager(RecyclerView recyclerView, Context ctx){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);

}





    private static boolean Contains(ArrayAdapter<String> adapter, String value) {
        boolean contains = true;
        try {

            contains = false;
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equals(value))
                    contains = true;
            }
        } catch (Exception e) {
        }
        return contains;
    }

    public static void updateTransport(String transport, ArrayAdapter<String> adapter) {
        if (!Contains(adapter, transport)) {
            adapter.add(transport);
            adapter.notifyDataSetChanged();
        }

    }


//    public static void transportauto(DbOperations dbOperations, SQLiteDatabase db, List<String> array, String name, Context ctx, AutoCompleteTextView txtname, ArrayAdapter<String> adapter)
//    {
//        try {
//            db = dbOperations.getWritableDatabase();
//            array = new ArrayList<>();
//
//            String sql = "SELECT distinct name  from AllCustomers";
//
//            Cursor cr = db.rawQuery(sql, null);
//            int i = 0;
//            while (cr.moveToNext()) {
//
//                name = cr.getString(cr.getColumnIndex("Name"));
//                array.add(name);
//                i++;
//            }
//
//            adapter = new ArrayAdapter<String>(ctx,
//                    android.R.layout.simple_list_item_1, array);
//            adapter.setNotifyOnChange(true);
//            txtname.setAdapter(adapter);
//        }catch(Exception e){}
//
//    }


    public static void Animations(Context ctx, @AnimRes int anim, Button... b){
        Animation animation = AnimationUtils.loadAnimation(ctx,anim);

        for(Button a: b){
           a.startAnimation(animation);
        }

    }



    public static void SetFullscreen(Window w){

        w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
       w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View decorView = w.getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

}
