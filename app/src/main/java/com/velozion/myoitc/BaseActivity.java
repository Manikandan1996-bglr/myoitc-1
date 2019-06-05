package com.velozion.myoitc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class BaseActivity extends AppCompatActivity {
    Snackbar snackbar;
    BroadcastReceiver receiver;


    public Toolbar toolbar;
    private boolean isToolbarRequired;
    private String toolbarTitle;
    private boolean isHomeMenuRequired;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);


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


        }


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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                overridePendingTransition(R.anim.left_in, R.anim.right_out);

                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }



    public boolean isToolbarRequired() {
        return isToolbarRequired;
    }


    public void setToolbarRequired(boolean toolbarRequired) {
        isToolbarRequired = toolbarRequired;
    }

    public String getToolbarTitle() {
        return toolbarTitle;
    }

    protected void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }


    public void setHomeMenuRequired(boolean homeMenuRequired) {
        isHomeMenuRequired = homeMenuRequired;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        if (isToolbarRequired) {
            View view = getLayoutInflater().inflate(layoutResID, null);

            View v = view.findViewById(R.id.toolbar);
            toolbar = v.findViewById(R.id.toolbar);

            toolbar.setTitle(toolbarTitle);

            toolbar.setTitleTextAppearance(this, R.style.ToolBarText);

            int color_combo=getRandomColor();
            toolbar.setBackgroundColor(color_combo);
            setSupportActionBar(toolbar);

           changeStatusbar(color_combo);


                if (getSupportActionBar() != null){

                    if (isHomeMenuRequired)
                    {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);
                    }else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                    }


                }




            super.setContentView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    void changeStatusbar(int color)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    public void activateSlideLeft()
    {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void activateSlideRight()
    {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


  /*  @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();

        switch (getRandomNumber())
        {
            case 1:
                theme.applyStyle(R.style.CustomeTheme1, true);
                break;

            case 2:
                theme.applyStyle(R.style.CustomeTheme2, true);
                break;

            case 3:
                theme.applyStyle(R.style.CustomeTheme3, true);
                break;

        }


        // you could also use a switch if you have many themes that could apply
        return theme;
    }*/


    private int getRandomNumber()
    {
        Random random=new Random();
       return random.nextInt(3);

    }

    public Toolbar getToolbar()
    {

        if (toolbar!=null)
        {
            return toolbar;
        }

        return null;
    }

}
