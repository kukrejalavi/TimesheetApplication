package com.example.timesheetapplication.Admin.java;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timesheetapplication.AddCostCenter;
import com.example.timesheetapplication.AddPayType;
import com.example.timesheetapplication.AssignAdmin;
import com.example.timesheetapplication.Sentmsg;
import com.example.timesheetapplication.Employee.java.AddTimesheetMainPage;
import com.example.timesheetapplication.Employee.java.FragmentNotificatioins;
import com.example.timesheetapplication.Employee.java.viewprojects;
import com.example.timesheetapplication.Login;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.navigation.BaseItem;
import com.example.timesheetapplication.navigation.CustomDataProvider;
import com.example.timesheetapplication.view.LevelBeamView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

public class AdminNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
TextView txtuser,txtemailid,txtnumber;
String empid,empname,empemailid,empnumber,supervisor;
    public static final String PREFS_NAME = "LoginPrefs";

View view;
    private MultiLevelListView multiLevelListView;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            if (((BaseItem) object).getName().equals("Assign Admin")) {
                displaySelectedScreen("Assign Admin");
            }
            if (((BaseItem) object).getName().contains("Add Timesheet")) {
                displaySelectedScreen("Add Timesheet");
            }
            if (((BaseItem) object).getName().contains("Approve Timesheet")) {
                displaySelectedScreen("Approve Timesheet");
            }
            if (((BaseItem) object).getName().contains("View Timesheet")) {
                displaySelectedScreen("View Timesheet");
            }
            if (((BaseItem) object).getName().contains("List")) {
                displaySelectedScreen("List");
            }
            if (((BaseItem) object).getName().equals("Assign")) {
                displaySelectedScreen("Assign");
            }
            if (((BaseItem) object).getName().contains("Sent")) {
                displaySelectedScreen("Sent");
            }
            if (((BaseItem) object).getName().contains("Add Paytype")) {
                displaySelectedScreen("Add Paytype");
            }
            if (((BaseItem) object).getName().contains("Inbox")) {
                displaySelectedScreen("Inbox");
            }
            if (((BaseItem) object).getName().contains("Compose")) {
                displaySelectedScreen("New Message");
            }
            if (((BaseItem) object).getName().contains("Cost Center")) {
                displaySelectedScreen("Cost Center");
            }
            if (((BaseItem) object).getName().contains("Add Project")) {
                displaySelectedScreen("Add Project");
            }
            if (((BaseItem) object).getName().contains("Assign Project")) {
                displaySelectedScreen("Assign Project");
            }
            if (((BaseItem) object).getName().contains("Current Projects")) {
                displaySelectedScreen("Current Projects");
            }

            if (((BaseItem) object).getName().contains("Calendar")) {
                displaySelectedScreen("Calendar");
            }
            if (((BaseItem) object).getName().contains("Employee")) {
                displaySelectedScreen("Employee");
            }

            if (((BaseItem) object).getName().contains("Add Employee")) {
                displaySelectedScreen("Add Employee");
            }
            if (((BaseItem) object).getName().contains("Review Employee")) {
                displaySelectedScreen("Review Employee");
            }
            if (((BaseItem) object).getName().contains("Edit Profile")) {
                displaySelectedScreen("Edit Profile");
            }

            if (((BaseItem) object).getName().contains("Add Category")) {
                displaySelectedScreen("Add Category");
            }
            if (((BaseItem) object).getName().contains("Log out")) {
                displaySelectedScreen("Log out");
            }
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);
      final Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       /// getSupportActionBar().setTitle("Admin");

        toolbar.setBackgroundColor(Color.rgb(72,120,170));

        Intent intent = getIntent();
        if(intent != null) {
            empid = intent.getStringExtra("id");
            empname = intent.getStringExtra("name");
            empemailid = intent.getStringExtra("emailid");
            empnumber = intent.getStringExtra("number");
            supervisor =    intent.getStringExtra("supervisor");
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(settings != null) {
            empid = settings.getString("id", "");
            empname = settings.getString("name", "");
              empemailid =    settings.getString("emailid","");
            empnumber =    settings.getString("number","");
          supervisor =   settings.getString("supervisor","");
        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();


        txtuser = (TextView) findViewById(R.id.name);
      txtemailid = (TextView) findViewById(R.id.emailid);
    txtnumber = (TextView) findViewById(R.id.number);

        txtuser.setText(empname);
        txtemailid.setText(empemailid);
        txtnumber.setText(empnumber);

        confMenu();

        displaySelectedScreen("Add Timesheet");
    }

    private void confMenu() {
        multiLevelListView = findViewById(R.id.multi_nav);
        // custom ListAdapter
        ListAdapter listAdapter = new ListAdapter();
        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener(mOnItemClickListener);
        if(supervisor!= null && supervisor!= "")
            listAdapter.setDataItems(CustomDataProvider.getInitialItemsSupervisor());
       else if(empemailid.equals("nilesh.peswani@revolvespl.com"))
            listAdapter.setDataItems(CustomDataProvider.getInitialItemsSuperAdmin());
        else
             listAdapter.setDataItems(CustomDataProvider.getInitialItems());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void displaySelectedScreen(String itemName) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemName) {

            case "Assign Admin":
                AssignAdmin assignAdmin = new AssignAdmin();
//                assignAdmin.empid = empid;
//                assignAdmin.empname = empname;
//                assignAdmin.logintype = "Admin";
                fragment = assignAdmin;
            break;
            case "Calendar":
                if(supervisor == null || supervisor == ""){
                com.example.timesheetapplication.CalendarView calendarView = new com.example.timesheetapplication.CalendarView();
                calendarView.logintype= "Admin";
                calendarView.empname=empname;
                calendarView.empid= empid;
                    fragment = calendarView;
                }
                else{
                    com.example.timesheetapplication.CalendarView calendarView = new com.example.timesheetapplication.CalendarView();
                    calendarView.logintype= "";
                    calendarView.empname=empname;
                    calendarView.empid= empid;
                    fragment = calendarView;
                }

                break;
            case "Add Timesheet":
                AddTimesheetMainPage addTimesheetMainPage = new AddTimesheetMainPage();
                addTimesheetMainPage.empid = empid;
                addTimesheetMainPage.empname = empname;
                addTimesheetMainPage.logintype = "Admin";
                fragment = addTimesheetMainPage;
                break;
            case "Approve Timesheet":
                AllTimesheets allTimesheets = new AllTimesheets();
                fragment = allTimesheets;
                break;
            case "View Timesheet":
                viewprojects Viewprojects = new viewprojects();
                Viewprojects.empid = empid;
                Viewprojects.empname = empname;
                fragment = Viewprojects;
                break;
            case "Add Project":
                fragment = new AddnewProject();
                break;
            case "Cost Center":
                fragment = new Costcenter();
                break;
            case "Assign Project":
                Assignprojandsv assignproj = new Assignprojandsv();
                assignproj.type = "Project";
                fragment = assignproj;
                break;
            case "Current Projects":
                fragment = new ProjectList();
                break;
            case "List":
                EmpandSvList suplist = new EmpandSvList();
                suplist.type = "Supervisor";
                fragment = suplist;
                break;
            case "Assign":
                Assignprojandsv assignsupervisor = new Assignprojandsv();
                assignsupervisor.type = "Supervisor";
                fragment = assignsupervisor;
                break;
            case "Employee":
                com.example.timesheetapplication.Admin.java.EmployeeList emp = new  com.example.timesheetapplication.Admin.java.EmployeeList();
                emp.type = "Employee";
                fragment = emp;
                break;
            case "Add Employee":
                CreateAccount createAccount = new CreateAccount();
                fragment = createAccount;
                break;
            case "Add Paytype":
                AddPayType addPayType = new AddPayType();

                fragment = addPayType;
                break;
            case "Add Category":
                AddCategory addCategory = new AddCategory();

                fragment = addCategory;
                break;
            case "Review Employee":
                com.example.timesheetapplication.Admin.java.EmployeeList emp1 = new  com.example.timesheetapplication.Admin.java.EmployeeList();
                emp1.type = "Employee";
                fragment = emp1;
                break;
            case "Edit Profile":
                Profile profile = new Profile();
                    profile.type = "Admin";
                    profile.empid = empid;
                fragment = profile;
                break;
            case "Sent":
                Sentmsg sentmsg =  new Sentmsg();
                sentmsg.type="Admin";
                sentmsg.empid=empid;
                fragment =sentmsg;
                break;
            case "Inbox":
                FragmentNotificatioins fragmentNotificatioins =         new FragmentNotificatioins();
                fragmentNotificatioins.type="Admin";
                fragmentNotificatioins.empid=empid;
                fragment =fragmentNotificatioins;
                break;
            case "New Message":
                ComposeMsg sendMsg = new  ComposeMsg();
                sendMsg.emaild = empemailid;
                sendMsg.empid = empid;
               if(supervisor != null && !supervisor.equals(""))
                   sendMsg.empname = empname;
                fragment = sendMsg;
                break;
            case "Log out":
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");
                    editor.remove("supervisor");
                    editor.commit();
                    finish();

                    Intent intent = new Intent(AdminNavigation.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
             //   fragment = new CreateAccount();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }


    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method display selected screen and passing the id of selected menu
        displaySelectedScreen(String.valueOf(item.getItemId()));
        //make this method blank
        return true;
    }


    private class ListAdapter extends MultiLevelListAdapter {

        @Override
        public List<?> getSubObjects(Object object) {
            return CustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return CustomDataProvider.isExpandable((BaseItem) object);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(AdminNavigation.this).inflate(R.layout.data_item, null);
                viewHolder.nameView = convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = convertView.findViewById(R.id.dataItemArrow);
                viewHolder.icon = convertView.findViewById(R.id.di_image);
                viewHolder.levelBeamView = convertView.findViewById(R.id.dataItemLevelBeam);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.ic_arrowdown : R.drawable.ic_arrowdown);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }
            viewHolder.icon.setImageResource(((BaseItem) object).getIcon());
            return convertView;
        }

        private class ViewHolder {
            TextView nameView;
            ImageView arrowView;
            ImageView icon;
            LevelBeamView levelBeamView;
        }
    }
}



