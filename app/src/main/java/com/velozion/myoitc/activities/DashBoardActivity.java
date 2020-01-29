package com.velozion.myoitc.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.DashBoardAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    DashBoardAdapter homeScreenAdapter;
    ArrayList<HashMap<String, Object>> taskmarray = new ArrayList<>();
    DrawerLayout drawer;
    NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout top_lin;
    private int userType;
    TextView user_name, welcome_msg, user_designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setToolbarTitle( "Myoitc" );
        setContentView( R.layout.activity_home );

        user_name = findViewById( R.id.txt_user_name );
        welcome_msg = findViewById( R.id.txt_welcome );
        user_designation = findViewById( R.id.txt_designation );
        top_lin = findViewById( R.id.top_lin );

        userType = PreferenceUtil.getUserType( "user_type", DashBoardActivity.this );

        if (userType == 1) {
            user_name.setText( "Joe Shestak" );
            user_designation.setText( "Employer" );
        } else if (userType == 2) {
            user_name.setText( "Henry Jurk" );
            user_designation.setText( "Employee" );
            top_lin.setBackgroundResource( R.drawable.employee_background );
        } else if (userType == 3) {
            user_name.setText( "William Brown" );
            user_designation.setText( "Manager" );
            top_lin.setBackgroundResource( R.drawable.manager_backgorund );
        } else if (userType == 4) {
            user_name.setText( "Logan Taylor" );
            user_designation.setText( "Supervisor" );
            top_lin.setBackgroundResource( R.drawable.supervisor_background );
        } else if (userType == 5) {
            user_name.setText( "John Smith" );
            user_designation.setText( "Provider" );
            top_lin.setBackgroundResource( R.drawable.provider_background );
        } else if (userType == 6) {
            user_name.setText( "Natasha" );
            user_designation.setText( "Client" );
            top_lin.setBackgroundResource( R.drawable.client_background );
        }

        welcome_msg.setText( Utils.getWelcomeMsg() );

        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener( this );

        recyclerView = findViewById( R.id.rcv_home_listing );

        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext(), 2 );
        recyclerView.setLayoutManager( gridLayoutManager );

        if (userType != 5 && userType != 6) {
            HashMap<String, Object> hasmap_taskmanagemnt = new HashMap<>();
            hasmap_taskmanagemnt.put( "name", "Task Management" );
            hasmap_taskmanagemnt.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_task_management ) );
            hasmap_taskmanagemnt.put( "pos", 0 );
            taskmarray.add( hasmap_taskmanagemnt );

            HashMap<String, Object> time_sheet = new HashMap<>();
            time_sheet.put( "name", "Time Sheet" );
            time_sheet.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_time_sheet ) );
            time_sheet.put( "pos", 1 );
            taskmarray.add( time_sheet );

            HashMap<String, Object> client_chart = new HashMap<>();
            client_chart.put( "name", "Client Chart" );
            client_chart.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_business ) );
            client_chart.put( "pos", 2 );
            taskmarray.add( client_chart );

            if (userType == 1 || userType == 2) {
                HashMap<String, Object> check_in = new HashMap<>();
                check_in.put( "name", "Check In" );
                check_in.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_map ) );
                check_in.put( "pos", 3 );
                taskmarray.add( check_in );
            }

            HashMap<String, Object> absent_tracker = new HashMap<>();
            absent_tracker.put( "name", "Absent Tracker" );
            absent_tracker.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_calendar ) );
            absent_tracker.put( "pos", 4 );
            taskmarray.add( absent_tracker );

            HashMap<String, Object> employee_folder = new HashMap<>();
            employee_folder.put( "name", "Employee Profile" );
            employee_folder.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_clients ) );
            employee_folder.put( "pos", 5 );
            taskmarray.add( employee_folder );

            if (userType != 2) {
                HashMap<String, Object> request_list = new HashMap<>();
                request_list.put( "name", "Request List" );
                request_list.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_business ) );
                request_list.put( "pos", 6 );
                taskmarray.add( request_list );
            }

            if (userType == 1) {
                HashMap<String, Object> choose_package = new HashMap<>();
                choose_package.put( "name", "Choose Package" );
                choose_package.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_choose_package ) );
                choose_package.put( "pos", 7 );
                taskmarray.add( choose_package );
            }
            if (userType == 2) {
                HashMap<String, Object> travelMap = new HashMap<>();
                travelMap.put( "name", "Travel Map" );
                travelMap.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_map ) );
                travelMap.put( "pos", 8 );
                taskmarray.add( travelMap );
            }
        }
        if (userType == 5) {
            HashMap<String, Object> provider_referralForm = new HashMap<>();
            provider_referralForm.put( "name", "Referral Service" );
            provider_referralForm.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_business ) );
            provider_referralForm.put( "pos", 9 );
            taskmarray.add( provider_referralForm );

            HashMap<String, Object> provider_serviceForm = new HashMap<>();
            provider_serviceForm.put( "name", "Referral Form" );
            provider_serviceForm.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_time_sheet ) );
            provider_serviceForm.put( "pos", 10 );
            taskmarray.add( provider_serviceForm );
        }

        if (userType == 6) {
            HashMap<String, Object> client_requestForm = new HashMap<>();
            client_requestForm.put( "name", "Request Service" );
            client_requestForm.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_task_management ) );
            client_requestForm.put( "pos", 11 );
            taskmarray.add( client_requestForm );

            HashMap<String, Object> client_referralForm = new HashMap<>();
            client_referralForm.put( "name", "Referral Form" );
            client_referralForm.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_business ) );
            client_referralForm.put( "pos", 12 );
            taskmarray.add( client_referralForm );

            HashMap<String, Object> client_treatmentPlans = new HashMap<>();
            client_treatmentPlans.put( "name", "Treatment/Care Plans" );
            client_treatmentPlans.put( "image", this.getResources().getDrawable( R.drawable.ic_svg_clients ) );
            client_treatmentPlans.put( "pos", 13 );
            taskmarray.add( client_treatmentPlans );
        }
        homeScreenAdapter = new DashBoardAdapter( this, taskmarray );
        recyclerView.setAdapter( homeScreenAdapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.dash_option, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer( GravityCompat.START );
                break;
            case R.id.menu_logout:
                LogOut();
                break;
            case R.id.menu_profile:
                startActivity( new Intent( getApplicationContext(), Profile2.class ) );
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.nav_task_management) {
            startActivity(new Intent(getApplicationContext(), TaskManagement.class));
        }*/
        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void LogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder( DashBoardActivity.this );
        builder.setMessage( "Do you want to Logout" );
        builder.setCancelable( true );
        builder.setNegativeButton( "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        } );

        builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceUtil.clearUser( getApplicationContext() );
                startActivity( new Intent( getApplicationContext(), LoginActivity.class ) );
                finish();
            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
            exitApp();
        } else {
            exitApp();
        }
    }

    private void exitApp() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText( this, "Please click back again to exit", Toast.LENGTH_SHORT ).show();
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 5000 );
    }
}
