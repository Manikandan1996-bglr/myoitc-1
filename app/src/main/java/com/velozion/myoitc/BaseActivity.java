package com.velozion.myoitc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
    Snackbar snackbar;
    BroadcastReceiver receiver;

    static ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar=getSupportActionBar();


         receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (!InternetConnection.checkConnection(getApplicationContext()))
                {
                   snackbar= Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet Connection", Snackbar.LENGTH_INDEFINITE);
                   snackbar.setAction("RETRY", new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           if (InternetConnection.checkConnection(getApplicationContext()))
                           {
                            snackbar.dismiss();
                           }else {
                               snackbar.show();
                           }
                       }
                   }).show();


                }else {


                    if (snackbar!=null)
                    {
                        snackbar.dismiss();
                    }

                }



            }
        };

        registerReceiver(receiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));



    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public static void setActionBarTitle(String s)
    {
        actionBar.setTitle(s);
    }

    public static void enableHomeUpButton(boolean value)
    {
        actionBar.setDisplayHomeAsUpEnabled(value);
    }

    public static void disableActionBar()
    {
        actionBar.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
