package com.velozion.myoitc.activities;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.ChoosePackageAdapter;
import com.velozion.myoitc.bean.ChoosePackageBeans;
import com.velozion.myoitc.databinding.ActivityChoosePackagesBinding;
import com.velozion.myoitc.interfaces.Listeners;
import com.velozion.myoitc.network.StringMethodRequest;
import com.velozion.myoitc.utilities.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChoosePackagesActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    ActivityChoosePackagesBinding choosePackagesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setToolbarTitle( "Choose Packages" );
        setHomeMenuRequired( false );
        setContentView( R.layout.activity_choose_packages );

        choosePackagesBinding = DataBindingUtil.setContentView( this, R.layout.activity_choose_packages );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(), RecyclerView.VERTICAL, false );
        choosePackagesBinding.rcvChoosePackages.setLayoutManager( linearLayoutManager );

        getPackageDetails();
    }

    private void getPackageDetails() {
        try {
            Map<String, String> mParams = new HashMap<>();
            StringMethodRequest postMethodRequest = new StringMethodRequest( this, Request.Method.GET, Utils.ChoosePackagesAPI, mParams, true, this, this, this, 1 );
            MyApplication.getInstance().addToRequestQueue( postMethodRequest );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<ChoosePackageBeans> choosePackageBeanArrayList) {
        ChoosePackageAdapter choosePackageAdapter = new ChoosePackageAdapter( this, choosePackageBeanArrayList );
        choosePackagesBinding.rcvChoosePackages.setAdapter( choosePackageAdapter );
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
                ArrayList<ChoosePackageBeans> choosePackageBeanArrayList = new ArrayList<>();
                JSONArray jsonArray = newJsObj.getJSONArray( "details" );
                for (int i = 0; i < jsonArray.length(); i++) {
                    ArrayList<String> permissionArray = new ArrayList<>();
                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                    JSONArray getPremissonArray = jsonObject.getJSONArray( "permissions" );
                    for (int j = 0; j < getPremissonArray.length(); j++) {
                        JSONObject permissionObj = getPremissonArray.getJSONObject( j );
                        permissionArray.add( permissionObj.getString( "name" ) );
                    }
                    choosePackageBeanArrayList.add( new ChoosePackageBeans( jsonObject.getString( "pacId" ),
                            jsonObject.getString( "pacName" ),
                            jsonObject.getString( "myoticCareMgmt" ),
                            jsonObject.getString( "clientsUpTo" ),
                            jsonObject.getString( "expireInDays" ),
                            jsonObject.getString( "jobsAllow" ),
                            jsonObject.getString( "price" ),
                            jsonObject.getString( "colorScheme" ),
                            permissionArray
                    ) );
                }
                setAdapter( choosePackageBeanArrayList );
            }
        }
    }
}
