package com.velozion.myoitc.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.ClientChartAdapter;
import com.velozion.myoitc.bean.ClientChartBean;
import com.velozion.myoitc.bean.ClientChartTaskListBean;
import com.velozion.myoitc.databinding.ActivityClientChartListBinding;
import com.velozion.myoitc.interfaces.Listeners;
import com.velozion.myoitc.network.StringMethodRequest;
import com.velozion.myoitc.utilities.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientChartActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    RecyclerView rcv_client_chart;
    ActivityClientChartListBinding activityClientChartListBinding;
    ArrayList<ClientChartBean> clientChartBeans;
    ClientChartAdapter client_chartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Client Chart" );

        clientChartBeans = new ArrayList<>();
        activityClientChartListBinding = DataBindingUtil.setContentView( this, R.layout.activity_client__chart_list );
        rcv_client_chart = findViewById( R.id.rcv_client_chart );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(), RecyclerView.VERTICAL, false );
        rcv_client_chart.setLayoutManager( linearLayoutManager );

        getClientChartList();
    }

    private void setAdapter(ArrayList<ClientChartBean> clientChartBeans) {
        client_chartAdapter = new ClientChartAdapter( ClientChartActivity.this, clientChartBeans );
        rcv_client_chart.setAdapter( client_chartAdapter );
    }

    private void getClientChartList() {
        try {
            Map<String, String> mParams = new HashMap<>();
            StringMethodRequest postMethodRequest = new StringMethodRequest( this, Request.Method.GET, Utils.ClientChartListAPI, mParams, true, this, this, this, 1 );
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
                JSONArray jsonArray = newJsObj.getJSONArray( "details" );
                ArrayList<ClientChartTaskListBean> clientChartTaskListBeans;
                for (int i = 0; i < jsonArray.length(); i++) {
                    clientChartTaskListBeans = new ArrayList<>();
                    JSONObject object = jsonArray.getJSONObject( i );
                    JSONArray clientTaskJsonArray1 = object.getJSONArray( "clientTasks" );
                    for (int j = 0; j < clientTaskJsonArray1.length(); j++) {
                        JSONObject object1 = clientTaskJsonArray1.getJSONObject( j );
                        clientChartTaskListBeans.add( new ClientChartTaskListBean( object1.getString( "taskName" ),
                                object1.getString( "scheduleDate" ),
                                object1.getString( "assignedTo" ),
                                object1.getString( "status" ) ) );
                    }
                    clientChartBeans.add( new ClientChartBean( object.getString( "clientId" ),
                            object.getString( "clientName" ),
                            object.getString( "cliMail" ),
                            object.getString( "cliMobile" ),
                            object.getString( "cliImage" ),
                            object.getString( "cliAddress" ),
                            object.getString( "cliState" ),
                            object.getString( "cliCity" ),
                            object.getString( "cliCountry" ),
                            object.getInt( "zipcode" ),
                            clientChartTaskListBeans
                    ) );
                }
                setAdapter( clientChartBeans );
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
                client_chartAdapter.getFilter().filter( s );
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
