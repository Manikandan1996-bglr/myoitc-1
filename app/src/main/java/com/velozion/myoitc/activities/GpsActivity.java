package com.velozion.myoitc.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.velozion.myoitc.R;

import java.util.Calendar;

public class GpsActivity extends AppCompatActivity {

    ImageView cancel;
    Button enable;
    LocationManager locationManager;

    private GoogleApiClient googleApiClient;
    public final static int REQUEST_LOCATION = 199;
    public final static int REQUEST_ENABLE_GPS = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeBasedOnTime();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        cancel = findViewById(R.id.gps_cancel);
        enable = findViewById(R.id.gps_enable);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });


        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                    finish();

                } else {//not enabled

                    enableLoc();

                }


            }
        });

    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            Log.d("ResponseLoc", "connected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                            Log.d("ResponseLoc", "connectedSuspended");
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            Log.d("ResponseLocationError", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());


            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {

                    final Status status = result.getStatus();

                    switch (status.getStatusCode()) {

                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.

                            Log.d("ResponseLoc", "responsesucces");


                            finish();


                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.d("ResponseLoc", "response_resolutionrequired");
                            try {

                                status.startResolutionForResult(GpsActivity.this, REQUEST_LOCATION);
                                //startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_LOCATION, null, 0, 0, 0, null);

                            } catch (IntentSender.SendIntentException e) {

                                // Ignore the error.
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            Log.d("ResponseLoc", "responsecheckunavailable");

                            break;
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:


                        finish();

                        break;
                    case Activity.RESULT_CANCELED:


                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, REQUEST_ENABLE_GPS);

                        break;
                    default:

                        Log.d("Response", "default activity result");
                        break;
                }
                break;

            case REQUEST_ENABLE_GPS:


                finish();

                break;
        }
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
