package com.velozion.myoitc.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               SharedPreferences sharedPreferences= getSharedPreferences(Utils.appName,MODE_PRIVATE);

               if (sharedPreferences.contains("userid"))
               {
                   startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                   finish();
               }else {
                   startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                   finish();
               }

            }
        }, 3000);


    }
}
