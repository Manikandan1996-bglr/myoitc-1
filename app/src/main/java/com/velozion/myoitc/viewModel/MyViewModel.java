package com.velozion.myoitc.viewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.bean.CheckInFormClientData;
import com.velozion.myoitc.bean.CheckInFormServiceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> Failuremessage = new MutableLiveData<>();
    private MutableLiveData<String> jsonError = new MutableLiveData<>();
    private MutableLiveData<String> volleyError = new MutableLiveData<>();

    private MutableLiveData<ArrayList<CheckInFormClientData>> ClientList;
    private MutableLiveData<ArrayList<CheckInFormServiceData>> ServicesList;


    //CLIENT LISTING FOR CHECK-IN FORM
    public LiveData<ArrayList<CheckInFormClientData>> getClientsList(final Context context) {

        if (ClientList == null) {
            ClientList = new MutableLiveData<>();
            final ArrayList<CheckInFormClientData> Data = new ArrayList<>();
            Map<String, String> jsonParams = new HashMap<>();
            RequestQueue requestQueue = Volley.newRequestQueue( context );

            CustomRequest customRequest = new CustomRequest( Request.Method.GET, Utils.ClientListAPI
                    , jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d( "ResponseS", response.toString() );
                            try {
                                if (response.getJSONArray( "Clients" ) != null) {
                                    JSONArray jsonArray = response.getJSONArray( "Clients" );

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject( i );

                                        CheckInFormClientData checkInFormClientData = new CheckInFormClientData();
                                        checkInFormClientData.setId( object.optString( "id" ) );
                                        checkInFormClientData.setName( object.optString( "name" ) );

                                        Data.add( checkInFormClientData );
                                    }
                                    ClientList.setValue( Data );
                                } else {
                                    String msg = response.getJSONObject( "messages" ).getJSONArray( "error" ).get( 0 ).toString();
                                    Toast.makeText( context, "" + msg, Toast.LENGTH_SHORT ).show();
                                    Failuremessage.setValue( msg );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText( context, "JsonException" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                jsonError.postValue( e.getMessage() );
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d( "ResponseE", volleyError.toString() );
                            Toast.makeText( context, "VolleyExce" + error.getMessage(), Toast.LENGTH_SHORT ).show();
                            volleyError.postValue( error.getMessage() );
                        }
                    } );
            requestQueue.add( customRequest );
        }
        return ClientList;
    }
    //SERVICE LISTING FOR CHECK-IN FORM
    public LiveData<ArrayList<CheckInFormServiceData>> getServicesList(final Context context) {
        if (ServicesList == null) {
            ServicesList = new MutableLiveData<>();
            final ArrayList<CheckInFormServiceData> Data = new ArrayList<>();
            Map<String, String> jsonParams = new HashMap<>();
            RequestQueue requestQueue = Volley.newRequestQueue( context );
            CustomRequest customRequest = new CustomRequest( Request.Method.GET, Utils.ServicesListAPI
                    , jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d( "ResponseS", response.toString() );
                            try {
                                if (response.getJSONArray( "Services" ) != null) {
                                    JSONArray jsonArray = response.getJSONArray( "Services" );
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject( i );
                                        CheckInFormServiceData checkInFormServiceData = new CheckInFormServiceData();
                                        checkInFormServiceData.setId( object.optString( "id" ) );
                                        checkInFormServiceData.setName( object.optString( "name" ) );

                                        Data.add( checkInFormServiceData );
                                    }
                                    ServicesList.setValue( Data );
                                } else {
                                    String msg = response.getJSONObject( "messages" ).getJSONArray( "error" ).get( 0 ).toString();
                                    Toast.makeText( context, "" + msg, Toast.LENGTH_SHORT ).show();
                                    Failuremessage.setValue( msg );
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText( context, "JsonException" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                jsonError.postValue( e.getMessage() );
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d( "ResponseE", volleyError.toString() );
                            Toast.makeText( context, "VolleyExce" + error.getMessage(), Toast.LENGTH_SHORT ).show();
                            volleyError.postValue( error.getMessage() );
                        }
                    } );
            requestQueue.add( customRequest );

        }
        return ServicesList;
    }


}
