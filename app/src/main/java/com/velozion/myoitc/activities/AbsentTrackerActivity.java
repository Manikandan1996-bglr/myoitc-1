package com.velozion.myoitc.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.bean.AbsentTrackerBean;
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

public class AbsentTrackerActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    List<Calendar> selected_dates = new ArrayList<>();

    SimpleDateFormat simpleDateFormat;
    CalendarView calendarView;

    ArrayList<AbsentTrackerBean> absentTrackerBeans;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setToolbarTitle( "Absent Tracker" );
        setHomeMenuRequired( false );
        setContentView( R.layout.activity_absent_tracker );
        absentTrackerBeans = new ArrayList<>();

        simpleDateFormat = new SimpleDateFormat( "dd-MM-YYYY" );
        calendarView = findViewById( R.id.calendarContainer );
        getAbsentTrackerList();

    }

    private void getAbsentTrackerList() {
        try {
            Map<String, String> mParams = new HashMap<>();
            StringMethodRequest postMethodRequest = new StringMethodRequest( this, Request.Method.GET, Utils.AbsentTrackerAPI, mParams, true, this, this, this, 1 );
            MyApplication.getInstance().addToRequestQueue( postMethodRequest );
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

    private void setCalendarView(List<EventDay> events, String date, String color) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendarView.setHeaderColor( R.color.grey );
            calendarView.setHeaderLabelColor( R.color.white );
            calendar.set( Calendar.DAY_OF_MONTH, Integer.parseInt( Utils.getDay( date ) ) );
            calendar.set( Calendar.MONTH, Utils.getMonthNum( date ) );
            calendar.set( Calendar.YEAR, Integer.parseInt( Utils.getyear( date ) ) );

            events.add( new EventDay( calendar, R.drawable.ic_leave, Color.parseColor( color ) ) );
            selected_dates.add( calendar );
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d( "ResponseCalenderError", e.getMessage() );
        }
        calendarView.setEvents( events );
        calendarView.setHighlightedDays( selected_dates );
    }

    @Override
    public void onPostCompleted(JSONObject newJsObj, int msgId) throws JSONException {
        if (msgId == 1) {
            if (newJsObj.getBoolean( "status" )) {
                List<EventDay> events = new ArrayList<>();
                JSONArray jsonArray = newJsObj.getJSONArray( "details" );
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject( i );

                    for (int j = 0; j < object.getJSONArray( "months" ).length(); j++) {
                        JSONObject monthJSONObj = object.getJSONArray( "months" ).getJSONObject( j );
                        for (int z = 0; z < monthJSONObj.getJSONArray( "holidays" ).length(); z++) {
                            JSONObject holidayJSONObj = monthJSONObj.getJSONArray( "holidays" ).getJSONObject( z );
                            String date = holidayJSONObj.getString( "date" );
                            String color_code = holidayJSONObj.getString( "reasonColorScheme" );
                            setCalendarView( events, date, color_code );
                        }
                    }
                    absentTrackerBeans.add( new AbsentTrackerBean( object.getString( "name" ),
                            object.getString( "designation" ),
                            object.getString( "month" ),
                            object.getString( "workingDays" ),
                            object.getString( "workingHrs" ) ) );
                }
            }
        }
    }
}
