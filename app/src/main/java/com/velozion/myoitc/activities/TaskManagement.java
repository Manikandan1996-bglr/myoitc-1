package com.velozion.myoitc.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.TaskAdapter;
import com.velozion.myoitc.bean.TaskManagementBean;
import com.velozion.myoitc.interfaces.Listeners;
import com.velozion.myoitc.network.StringMethodRequest;
import com.velozion.myoitc.utilities.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagement extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    CalendarView calendarView;
    TextView date_textview;
    RecyclerView recyclerView;
    LinearLayout no_data_found;

    ArrayList<TaskManagementBean> taskManagementBeanArrayList = new ArrayList<>();

    TaskAdapter taskAdapter;

    List<Calendar> selected_dates = new ArrayList<>();

    SimpleDateFormat simpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setToolbarTitle("Task Management");
        setHomeMenuRequired(false);
        setContentView(R.layout.activity_task_management);

        simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");

        calendarView = findViewById(R.id.calendarView);
        date_textview = findViewById(R.id.date);
        recyclerView = findViewById(R.id.task_recyclerview);
        no_data_found = findViewById(R.id.nodata_ll);
        taskAdapter = new TaskAdapter(getApplicationContext());
        recyclerView.setAdapter(taskAdapter);

        getTaskList();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                HandleList(simpleDateFormat.format(calendar.getTime()));
            }
        });
    }

    private void HandleToday() {
        Calendar calendar = Calendar.getInstance();
        HandleList(simpleDateFormat.format(calendar.getTime()));
    }

    void HandleList(String date) {

        Log.d("ResponseDate", date);
        ArrayList<TaskManagementBean> encountered = new ArrayList<>();
        if (taskManagementBeanArrayList.size() > 0) {
            for (TaskManagementBean taskManagementBean : taskManagementBeanArrayList) {
                if (taskManagementBean.getDate().equalsIgnoreCase(date)) {
                    encountered.add(taskManagementBean);
                }
            }
            if (encountered.size() > 0) {
                date_textview.setText(date);
                taskAdapter.addTasks(encountered);
                date_textview.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                no_data_found.setVisibility(View.GONE);
            } else {
                date_textview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                no_data_found.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getTaskList() {
        try {
            Map<String, String> mParams = new HashMap<>();

            StringMethodRequest postMethodRequest = new StringMethodRequest(this, Request.Method.GET, Utils.TaskManagementAPI, mParams, true, this, this, this, 1);
            MyApplication.getInstance().addToRequestQueue(postMethodRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
    }

    private void setCalendarView(ArrayList<TaskManagementBean> taskManagementBeans) {
        if (taskManagementBeans.size() > 0) {
            HandleToday();
            List<EventDay> events = new ArrayList<>();
            for (TaskManagementBean task : taskManagementBeans) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Utils.getDay(task.getDate())));
                    calendar.set(Calendar.MONTH, Utils.getMonthNum(task.getDate()));
                    calendar.set(Calendar.YEAR, Integer.parseInt(Utils.getyear(task.getDate())));

                    events.add(new EventDay(calendar, R.drawable.ic_svg_task_management, Color.parseColor("#40B845")));
                    selected_dates.add(calendar);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("ResponseCalenderError", e.getMessage());
                }
            }
            calendarView.setEvents(events);
            calendarView.setHighlightedDays(selected_dates);
        } else {
            Toast.makeText(TaskManagement.this, "No Task Found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPostCompleted(JSONObject newJsObj, int msgId) throws JSONException {
        if (msgId == 1) {
            if (newJsObj.getBoolean("status")) {
                JSONArray jsonArray = newJsObj.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    taskManagementBeanArrayList.add(new TaskManagementBean(object.getString("clientName"),
                            object.getString("serviceType"),
                            object.getString("assignedBy"),
                            object.getString("address"),
                            object.getString("startTime"),
                            object.getString("endTime"),
                            object.getString("company"),
                            object.getString("date"),
                            object.getString("lat"),
                            object.getString("long")));
                }
                setCalendarView(taskManagementBeanArrayList);
            }
        }
    }
}
