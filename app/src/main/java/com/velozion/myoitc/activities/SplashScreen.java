package com.velozion.myoitc.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.velozion.myoitc.InternetConnection;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

public class SplashScreen extends AppCompatActivity {

    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!InternetConnection.checkConnection(SplashScreen.this)) {


                    startActivityForResult(new Intent(getApplicationContext(), InternetActivity.class), 100);

                } else {

                    donext();

                }


            }
        }, 3000);


    }

    private void showSnackBar() {


        snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    snackbar.dismiss();
                    donext();
                } else {
                    snackbar.show();
                }
            }
        }).show();


    }

    void donext() {
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.appName, MODE_PRIVATE);

        if (sharedPreferences.contains("userid")) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    donext();
                }

            } else if (resultCode == RESULT_CANCELED) {

                showSnackBar();
            }

        }
    }

}
