package com.velozion.myoitc.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.TimeSheetAdapter;
import com.velozion.myoitc.bean.TimeSheetBean;
import com.velozion.myoitc.interfaces.Listeners;
import com.velozion.myoitc.network.StringMethodRequest;
import com.velozion.myoitc.utilities.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeSheetActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    RecyclerView rcv_time_sheet;
    ArrayList<TimeSheetBean> timeSheetBeanArrayList = new ArrayList<>();
    TimeSheetAdapter timeSheetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setToolbarRequired( true );
        setToolbarTitle( getString( R.string.time_sheet ) );
        setHomeMenuRequired( false );
        setContentView( R.layout.activity_time_sheet );

        rcv_time_sheet = findViewById( R.id.rcv_time_sheet );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(), RecyclerView.VERTICAL, false );
        rcv_time_sheet.setLayoutManager( linearLayoutManager );
        getTimeSheetValue();

    }


    private void setAdapter() {
        timeSheetAdapter = new TimeSheetAdapter( TimeSheetActivity.this, timeSheetBeanArrayList );
        rcv_time_sheet.setAdapter( timeSheetAdapter );
    }

    private void getTimeSheetValue() {
        try {
            Map<String, String> mParams = new HashMap<>();
            StringMethodRequest postMethodRequest = new StringMethodRequest( this, Request.Method.GET, Utils.TimeSheetAPI, mParams, true, this, this, this, 1 );
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

    @Override
    public void onPostCompleted(JSONObject newJsObj, int msgId) throws JSONException {

        if (msgId == 1) {
            if (newJsObj.getBoolean( "status" )) {
                timeSheetBeanArrayList = new ArrayList<>();
                JSONArray jsonArray = newJsObj.getJSONArray( "details" );
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                    timeSheetBeanArrayList.add( new TimeSheetBean( jsonObject.getString( "date" ),
                            jsonObject.getString( "day" ),
                            jsonObject.getString( "timeIn" ),
                            jsonObject.getString( "timeOut" ),
                            jsonObject.getString( "payCode" ),
                            jsonObject.getString( "serviceType" ),
                            jsonObject.getString( "clientInitial" ),
                            jsonObject.getString( "clientDesignee" ),
                            jsonObject.getString( "totalHours" ),
                            jsonObject.getString( "verifiedBy" ),
                            jsonObject.getString( "verifiedOn" ),
                            jsonObject.getString( "status" ),
                            jsonObject.getString( "employee" ),
                            jsonObject.getString( "checkinAddress" ) ) );
                }
                setAdapter();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.search_menu, menu );
        MenuItem menuItem = menu.findItem( R.id.search_bar );
        final SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                timeSheetAdapter.getFilter().filter( s );
                searchView.setQueryHint( getResources().getString( R.string.search_bar_hint ) );
                int searchSrcTextId = getResources().getIdentifier( "android:id/search_src_text", null, null );
                EditText searchEditText = searchView.findViewById( searchSrcTextId );
                searchEditText.setTextColor( Color.WHITE );
                searchEditText.setHintTextColor( Color.LTGRAY );
                return true;
            }
        } );
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_bar:
                if (item.getItemId() == R.id.search_bar) {
                    return true;
                }
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }


}
