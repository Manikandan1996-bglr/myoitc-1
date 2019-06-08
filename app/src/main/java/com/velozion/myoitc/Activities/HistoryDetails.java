package com.velozion.myoitc.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.databinding.ActivityHistoryDetailsBinding;
import com.velozion.myoitc.db.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryDetails extends BaseActivity {

    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;

    ActivityHistoryDetailsBinding historyDetailsBinding;
    HistoryData historyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.CustomeTheme5);
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setToolbarTitle(getResources().getString(R.string.activity_historydetails));
        historyDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_details);

        historyData = getIntent().getExtras().getParcelable("data");

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //mMap.getUiSettings().setScrollGesturesEnabled(false);
                //mMap.getUiSettings().setAllGesturesEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);


                if (!historyData.getCheckinLat().equals("null") && !historyData.getCheckinLang().equals("null")) {
                    LatLng chekinloc = new LatLng(Double.parseDouble(historyData.getCheckinLat()), Double.parseDouble(historyData.getCheckinLang()));
                    mMap.addMarker(new MarkerOptions().position(chekinloc).title("Check In").snippet(historyData.getCheckinloc()));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(chekinloc));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(chekinloc, 10, 15, 0)));

                }

                if (!historyData.getCheckoutLan().equals("null") && !historyData.getCheckoutLang().equals("null")) {
                    LatLng chekoutloc = new LatLng(Double.parseDouble(historyData.getCheckoutLan()), Double.parseDouble(historyData.getCheckoutLang()));
                    mMap.addMarker(new MarkerOptions().position(chekoutloc).title("Check Out").snippet(historyData.getCheckoutloc()));
                    Log.d("Response", "Desinationmarker added");

                }


                if (!historyData.getCheckinLat().equals("null") && !historyData.getCheckinLang().equals("null") && !historyData.getCheckoutLan().equals("null") && !historyData.getCheckoutLang().equals("null")) {

                    StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
                    googleDirectionsUrl.append("origin=" + historyData.getCheckinLat() + "," + historyData.getCheckinLang());
                    googleDirectionsUrl.append("&destination=" + historyData.getCheckoutLan() + "," + historyData.getCheckoutLang());
                    googleDirectionsUrl.append("&key=" + getResources().getString(R.string.google_key));

                    Log.d("url", googleDirectionsUrl.toString());
                    getResponseFromUrl(String.valueOf(googleDirectionsUrl));

                }


               /* CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(chekinloc)
                        .zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//set format of date you receiving from db

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");


        if (!historyData.getCheckintime().equals("null")) {
            String checkin = historyData.getCheckintime();


            try {
                Date data1 = sdf.parse(checkin);

                historyData.setCheckintime("" + date.format(data1) + "\n" + time.format(data1));
                //checkintime.setText(""+date.format(data1)+"\n"+time.format(data1));

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

        if (!historyData.getCheckouttime().equals("null")) {
            String checkout = historyData.getCheckouttime();

            try {
                Date data2 = sdf.parse(checkout);

                historyData.setCheckouttime("" + date.format(data2) + "\n" + time.format(data2));
                // checkouttime.setText();


            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        historyDetailsBinding.setHistory(historyData);


    }


    private void getResponseFromUrl(String url) {


        Utils.displayCustomDailog(HistoryDetails.this);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("response", url);


        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("response", response);

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("REQUEST_DENIED")) {

                        Toast.makeText(HistoryDetails.this, "" + status, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("ZERO_RESULTS")) {
                        Toast.makeText(HistoryDetails.this, "Results Not Found", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
                        Toast.makeText(HistoryDetails.this, "" + status, Toast.LENGTH_SHORT).show();

                    } else {

                        if (jsonObject.getJSONArray("routes").length() > 0) {
                            String[] directionsList;
                            directionsList = parseDirections(response);

                            displayDirection(directionsList);
                        }

                    }

                    Utils.dismissCustomDailog();

                } catch (Exception e) {

                    Toast.makeText(HistoryDetails.this, "exception:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Utils.dismissCustomDailog();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissCustomDailog();

                Toast.makeText(HistoryDetails.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(stringRequest);


    }


    public String[] parseDirections(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");


            Log.d("steparray", jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJson) {

        int count = googleStepsJson.length();

        String[] polylines = new String[count];

        try {

            for (int i = 0; i < count; i++) {
                try {
                    polylines[i] = getPath(googleStepsJson.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Steps exception" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


        return polylines;
    }

    public String getPath(JSONObject googlePathJson) {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }


    public void displayDirection(String[] directionsList) {

        int count = directionsList.length;

        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLUE);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));
            mMap.addPolyline(options);


            float[] results = new float[10];

            // Location.distanceBetween(data.get(0).latitude,data.get(0).longitude,data.get(1).latitude,data.get(1).longitude,results);

           /* float Kms=results[0]/1000;
            destMarker.snippet("Distance:"+Kms+" Kms");

            shippingCharges=Kms*20;

            distanceTextview.setText("Distance : "+Kms+" Kms");
            shippingTextview.setText("Shipping Charges:"+shippingCharges+" Rs");*/


        }
    }

}
