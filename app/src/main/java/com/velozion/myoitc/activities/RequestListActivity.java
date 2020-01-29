package com.velozion.myoitc.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.RequestListAdapter;
import com.velozion.myoitc.bean.RequestServicesBean;
import com.velozion.myoitc.databinding.ActivityRequestlistBinding;
import com.velozion.myoitc.interfaces.Listeners;
import com.velozion.myoitc.network.StringMethodRequest;
import com.velozion.myoitc.utilities.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestListActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    private ArrayList<RequestServicesBean> requestServices;
    private ArrayList<String> arrayclientnames, arrayclientreferral, arrayclientcity;
    private String get_client_name, get_client_referal, get_client_city;
    private Spinner spr_client_name, spr_client_city, spr_client_refferal;

    ActivityRequestlistBinding activityRequestlistBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Request Services" );

        activityRequestlistBinding = DataBindingUtil.setContentView( this, R.layout.activity_requestlist );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(), RecyclerView.VERTICAL, false );
        activityRequestlistBinding.requestrecyclerview.setLayoutManager( linearLayoutManager );

        activityRequestlistBinding.fabfilterlist.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFliterValues();
            }
        } );
        requestServiceList();
    }

    private void setAdapter() {
        RequestListAdapter requestListAdapter = new RequestListAdapter( RequestListActivity.this, requestServices );
        activityRequestlistBinding.requestrecyclerview.setAdapter( requestListAdapter );
    }

    private void setFliterValues() {
        final Dialog dialog = new Dialog( this );
        dialog.setContentView( R.layout.activity_requestfilterlist );
        dialog.show();
        spr_client_name = dialog.findViewById( R.id.spinnerclientnames );
        spr_client_city = dialog.findViewById( R.id.spinnerclientcities );
        spr_client_refferal = dialog.findViewById( R.id.spinnerreferralnames );

        activityRequestlistBinding.fabfilterlist.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spr_client_name.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        get_client_name = spr_client_name.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                } );

                spr_client_refferal.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        get_client_referal = spr_client_refferal.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                } );

                spr_client_city.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        get_client_city = spr_client_city.getSelectedItem().toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                } );
            }
        } );

        ArrayAdapter<String> adapter = new ArrayAdapter<>( RequestListActivity.this, android.R.layout.simple_spinner_item, arrayclientnames );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, arrayclientreferral );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, arrayclientcity );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spr_client_name.setAdapter( adapter );
        spr_client_refferal.setAdapter( adapter1 );
        spr_client_city.setAdapter( adapter2 );
    }


    private void requestServiceList() {
        try {
            Map<String, String> mParams = new HashMap<>();
            StringMethodRequest postMethodRequest = new StringMethodRequest( RequestListActivity.this, Request.Method.GET, Utils.RequestListAPI, mParams, true, this, this, this, 1 );
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
                requestServices = new ArrayList<>();
                arrayclientnames = new ArrayList<>();
                arrayclientreferral = new ArrayList<>();
                arrayclientcity = new ArrayList<>();
                JSONArray jsonArray = newJsObj.getJSONArray( "details" );
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject( i );

                    requestServices.add( new RequestServicesBean( jsonObject.getString( "referalDate" ),
                            jsonObject.getString( "clientName" ),
                            jsonObject.getString( "serviceType" ),
                            jsonObject.getString( "referalSource" ),
                            jsonObject.getString( "city" ),
                            jsonObject.getString( "state" ),
                            jsonObject.getString( "assignedEmployee" ),
                            jsonObject.getString( "status" )
                    ) );
                    arrayclientnames.add( jsonObject.getString( "clientName" ) );
                    arrayclientreferral.add( jsonObject.getString( "referalSource" ) );
                    arrayclientcity.add( jsonObject.getString( "city" ) );
                }
                setAdapter();
            }
        }
    }


}
