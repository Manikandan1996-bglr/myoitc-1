package com.velozion.myoitc.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.velozion.myoitc.InternetConnection;
import com.velozion.myoitc.R;

import java.util.Calendar;

public class InternetActivity extends AppCompatActivity {

    ImageView cancel;
    Button enable;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeBasedOnTime();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);


        cancel = findViewById(R.id.ic_cancelimg);
        enable = findViewById(R.id.ic_enable);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });


        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (InternetConnection.checkConnection(getApplicationContext())) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 100);

                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone","com.android.phone.NetworkSetting");
                    startActivityForResult(intent,100);*/
                }


            }
        });

        EventListner();

    }

    private void EventListner() {


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (InternetConnection.checkConnection(getApplicationContext())) {

                    finishActivity();

                }


            }
        };

        registerReceiver(receiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    setResult(RESULT_OK);
                    finish();
                }

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "You Didn't Turned Internet", Toast.LENGTH_SHORT).show();

            }

        }
    }

    void finishActivity() {
        setResult(RESULT_OK);
        finish();
    }

    void ThemeBasedOnTime() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            setTheme(R.style.MorningSession);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            setTheme(R.style.AfternoonSession);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            setTheme(R.style.EveningSession);
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            setTheme(R.style.NightSession);
        } else {
            setTheme(R.style.DefaultSession);
        }

    }
}
