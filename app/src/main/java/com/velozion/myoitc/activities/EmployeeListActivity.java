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
import com.velozion.myoitc.adapter.EmployeeListAdapter;
import com.velozion.myoitc.bean.EmployeeListBean;
import com.velozion.myoitc.interfaces.Listeners;
import com.velozion.myoitc.network.StringMethodRequest;
import com.velozion.myoitc.utilities.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeListActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String>, Listeners.POSTMethodListener {

    RecyclerView rcv_employe_list;
    ArrayList<EmployeeListBean> employeeListBeans;
    private int from = 0;
    EmployeeListAdapter employeeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Employee List" );
        setContentView( R.layout.activity_employee_list );
        rcv_employe_list = findViewById( R.id.rcv_employe_list );

        from = getIntent().getIntExtra( "from", 0 );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(), RecyclerView.VERTICAL, false );
        rcv_employe_list.setLayoutManager( linearLayoutManager );

        getEmployeeList();
    }

    private void setAdapter() {
        employeeListAdapter = new EmployeeListAdapter( EmployeeListActivity.this, employeeListBeans, from );
        rcv_employe_list.setAdapter( employeeListAdapter );
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
                employeeListBeans = new ArrayList<>();
                JSONArray jsonArray = newJsObj.getJSONArray( "details" );
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                    EmployeeListBean employeeListBean = new EmployeeListBean();
                    employeeListBean.setEmp_name( jsonObject.getString( "name" ) );
                    employeeListBean.setEmp_designation( jsonObject.getString( "designation" ) );
                    JSONArray jsonElements = jsonObject.getJSONArray( "months" );
                    for (int j = 0; j < jsonElements.length(); j++) {
                        JSONObject jsonObject1 = jsonElements.getJSONObject( j );
                        employeeListBean.setEmp_total( jsonObject1.getString( "workingDays" ) );
                        employeeListBean.setEmp_work_hrs( jsonObject1.getString( "workingHrs" ) );
                        JSONArray jsonleavecount = jsonObject1.getJSONArray( "holidays" );
                        String leaveocunt = String.valueOf( jsonleavecount.length() );
                        employeeListBean.setEmp_leave( leaveocunt );
                    }
                    employeeListBeans.add( employeeListBean );
                }
                setAdapter();
            }
        }
    }

    private void getEmployeeList() {
        try {
            Map<String, String> mParams = new HashMap<>();
            StringMethodRequest postMethodRequest = new StringMethodRequest( this, Request.Method.GET, Utils.EmployeeListAPI, mParams, true, this, this, this, 1 );
            MyApplication.getInstance().addToRequestQueue( postMethodRequest );
        } catch (Exception e) {
            e.printStackTrace();
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
                employeeListAdapter.getFilter().filter( s );
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
