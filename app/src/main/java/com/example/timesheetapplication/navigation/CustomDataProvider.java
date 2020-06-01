package com.example.timesheetapplication.navigation;


import android.content.Context;

import com.example.timesheetapplication.R;

import java.util.ArrayList;
import java.util.List;


public class CustomDataProvider {

    private static final int MAX_LEVELS = 3;

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;
    private static List<BaseItem> mMenu = new ArrayList<>();
    Context context;

    public static List<BaseItem> getInitialItems() {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();

        rootMenu.add(new GroupItem("Timesheet", R.drawable.ic_timesheet));
        rootMenu.add(new GroupItem("Project", R.drawable.ic_explore));
        rootMenu.add(new GroupItem("Supervisor", R.drawable.ic_supervisor));
        rootMenu.add(new Item("Cost Center", R.drawable.ic_costcenter));
        rootMenu.add(new GroupItem("Add Account", R.drawable.ic_addaccount));
        rootMenu.add(new Item("Add Paytype", R.drawable.ic_paytype));
        rootMenu.add(new Item("Edit Profile", R.drawable.ic_profile));
        rootMenu.add(new GroupItem("Notifications", R.drawable.ic_notification));
        rootMenu.add(new Item("Calendar", R.drawable.ic_calendar));
        rootMenu.add(new Item("Log out", R.drawable.ic_logout));
        return rootMenu;
    }


    public static List<BaseItem> getInitialItemsSuperAdmin() {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();

        rootMenu.add(new GroupItem("Timesheet", R.drawable.ic_timesheet));
        rootMenu.add(new GroupItem("Project", R.drawable.ic_explore));
        rootMenu.add(new GroupItem("Supervisor", R.drawable.ic_supervisor));
        rootMenu.add(new Item("Assign Admin", R.drawable.ic_assignadmin));
        rootMenu.add(new Item("Cost Center", R.drawable.ic_costcenter));
        rootMenu.add(new GroupItem("Add Account", R.drawable.ic_addaccount));
        rootMenu.add(new Item("Add Paytype", R.drawable.ic_paytype));
        rootMenu.add(new Item("Edit Profile", R.drawable.ic_profile));
        rootMenu.add(new GroupItem("Notifications", R.drawable.ic_notification));
        rootMenu.add(new Item("Calendar", R.drawable.ic_calendar));
        rootMenu.add(new Item("Log out", R.drawable.ic_logout));
        return rootMenu;
    }


    public static List<BaseItem> getInitialItemsSupervisor() {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();

        rootMenu.add(new GroupItem("Timesheet", R.drawable.ic_timesheet));
        rootMenu.add(new Item("Employee", R.drawable.ic_employee));
        rootMenu.add(new Item("Edit Profile", R.drawable.ic_profile));
        rootMenu.add(new GroupItem("Notifications", R.drawable.ic_notification));
        rootMenu.add(new Item("Calendar", R.drawable.ic_calendar));
        rootMenu.add(new Item("Log out", R.drawable.ic_logout));
        return rootMenu;
    }

    public static List<BaseItem> getSubItems(BaseItem baseItem) {

        List<BaseItem> result = new ArrayList<>();
        int level = ((GroupItem) baseItem).getLevel() + 1;
        String menuItem = baseItem.getName();

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }

        if (level == LEVEL_1) {
            switch (menuItem.toUpperCase()) {
                case "TIMESHEET":
                    result = getListTimesheets();
                    break;
                case "PROJECT":
                    result = getListProjects();
                    break;
                case "SUPERVISOR":
                    result = getListSupervisors();
                    break;

                case "NOTIFICATIONS":
                    result = getListNotificationss();
                    break;

                case "ADD ACCOUNT":
                    result = getListAddAccount();
                    break;
            }
        }
        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }

    private static List<BaseItem> getListTimesheets() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Add Timesheet"));
        list.add(new Item("Approve Timesheet"));
        list.add(new Item("View Timesheet"));


        return list;
    }

    private static List<BaseItem> getListSupervisors() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("List"));
        list.add(new Item("Assign"));


        return list;
    }


    private static List<BaseItem> getListAddAccount() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Add Employee"));
        list.add(new Item("Review Employee"));
        return list;
    }

    private static List<BaseItem> getListNotificationss() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Compose"));
        list.add(new Item("Inbox"));
        list.add(new Item("Sent"));
        return list;
    }

    private static List<BaseItem> getListProjects() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Add Project"));
        list.add(new Item("Add Category"));
        list.add(new Item("Assign Project"));
        list.add(new Item("Current Projects"));
        return list;
    }
}
