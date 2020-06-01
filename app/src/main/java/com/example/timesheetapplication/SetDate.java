package com.example.timesheetapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetDate {
  static  Calendar mCurrentDate;
    static  String day, month, year, hour, min, sec;

    public static String Date(final EditText txtdate, final Context ctx){


        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));


        if (hour.length() == 1)
            hour = "0" + hour;

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;


        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

String datetime = day + "/" + month + "/" + year;
        txtdate.setText(datetime);





        txtdate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofyear, int dayOfMonth) {
                        monthofyear = monthofyear + 1;
                        if (monthofyear < 10) {
                            if (dayOfMonth < 10)

                                txtdate.setText("0" + dayOfMonth + "/0" + monthofyear + "/" + year);
                            else
                                txtdate.setText(dayOfMonth + "/0" + monthofyear + "/" + year);
                        } else {
                            if (dayOfMonth < 10)

                                txtdate.setText("0" + dayOfMonth + "/" + monthofyear + "/" + year);
                            else
                                txtdate.setText(dayOfMonth + "/" +  monthofyear + "/" + year);
                        }
                    }
                }, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePickerDialog.show();
            }
        });

return datetime;
    }

    public static String Date(final TextView txtdate, final Context ctx){


        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));


        if (hour.length() == 1)
            hour = "0" + hour;

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;


        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

        String datetime = day + "/" + month + "/" + year;
        txtdate.setText(datetime);





        txtdate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofyear, int dayOfMonth) {
                        monthofyear = monthofyear + 1;
                        if (monthofyear < 10) {
                            if (dayOfMonth < 10)

                                txtdate.setText("0" + dayOfMonth + "/0" + monthofyear + "/" + year);
                            else
                                txtdate.setText(dayOfMonth + "/0" + monthofyear + "/" + year);
                        } else {
                            if (dayOfMonth < 10)

                                txtdate.setText("0" + dayOfMonth + "/" + monthofyear + "/" + year);
                            else
                                txtdate.setText(dayOfMonth + "/" +  monthofyear + "/" + year);
                        }
                    }
                }, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePickerDialog.show();
            }
        });

        return datetime;
    }

    public static String withoutonclickDate(final EditText txtdate, final Context ctx){


        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));


        if (hour.length() == 1)
            hour = "0" + hour;

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;


        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

        String datetime = day + "/" + month + "/" + year;
        txtdate.setText(datetime);

        return datetime;
    }

    public static String withoutonclickDate(final TextView txtdate, final Context ctx){


        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));


        if (hour.length() == 1)
            hour = "0" + hour;

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;


        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

        String datetime = day + "/" + month + "/" + year;
        txtdate.setText(datetime);

        return datetime;
    }

    public static String Currentdate(){
        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
//        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
//        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
//        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));
//

//        if (hour.length() == 1)
//            hour = "0" + hour;
//
//        if (min.length() == 1)
//            min = "0" + min;
//
//        if (sec.length() == 1)
//            sec = "0" + sec;
//

        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

        String datetime = day + "/" + month + "/" + year;

        return datetime;
    }

    public static String Currentdatewithmonthwithout0(){
        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
//        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
//        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
//        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));
//

//        if (hour.length() == 1)
//            hour = "0" + hour;
//
//        if (min.length() == 1)
//            min = "0" + min;
//
//        if (sec.length() == 1)
//            sec = "0" + sec;
//

        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {

                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);

        }

        String datetime = day + "/" + month + "/" + year;

        return datetime;
    }

    public static  String Currentdateonlydd(){
        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));

        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        return day;
    }

    public static String Currentdateinyyyymmdd(){
        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));


        if (hour.length() == 1)
            hour = "0" + hour;

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;


        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

        String datetime = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;

        return datetime;
    }

    public static String Currentdateinyyyymmddwithouttime(){
        mCurrentDate = Calendar.getInstance();
        day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
        year = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        hour = String.valueOf(mCurrentDate.get(Calendar.HOUR));
        min = String.valueOf(mCurrentDate.get(Calendar.MINUTE));
        sec = String.valueOf(mCurrentDate.get(Calendar.SECOND));


        if (hour.length() == 1)
            hour = "0" + hour;

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;


        if (day.length() == 1) {
            day = "0" + (Integer.parseInt(day));
        } else {
            day = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        }

        if(Integer.parseInt(month) == 9) {
            month = Integer.parseInt(month) + 1 + "";
        }
        else {
            if (month.length() == 1) {


                month = "0" + (Integer.parseInt(month) + 1);
            } else {
                month = String.valueOf(mCurrentDate.get(Calendar.MONTH));
                month = String.valueOf(Integer.parseInt(month) + 1);
            }
        }

        String datetime = year + "-" + month + "-" + day;

        return datetime;
    }

    public static String dateplusone(EditText txtdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(txtdate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH,+1 );
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }


    public static String dateplusoneyear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(SetDate.Currentdateinyyyymmddwithouttime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.YEAR,+1 );
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }


    public static String getMonth(){
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }

    public static String getyear(){
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("YYYY");
        String year = month_date.format(cal.getTime());
        return year;
    }


    public static String dateplus(EditText txtdate,int days){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(txtdate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH,+days );
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }



    public static String dateminus(String txtdate,int days){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(txtdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH,-days );
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }


    public static String dateminusone(EditText txtdate){
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Calendar c = Calendar.getInstance();
    try {
        //Setting the date to the given date
        c.setTime(sdf.parse(txtdate.getText().toString()));
    } catch (ParseException e) {
        e.printStackTrace();
    }

    //Number of Days to add
    c.add(Calendar.DAY_OF_MONTH, -1);
    //Date after adding the days to the given date
    String newDate = sdf.format(c.getTime());
    return newDate;
}


}
