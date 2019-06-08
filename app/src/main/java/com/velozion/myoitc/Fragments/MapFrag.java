package com.velozion.myoitc.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.velozion.myoitc.Activities.HomeActivity;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class MapFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;

    private GoogleApiClient googleApiClient;
    public final static int REQUEST_LOCATION = 199;
    public final static int REQUEST_ENABLE_GPS=300;

    LocationManager manager;

    double latitude, longitude;
    String locationAddres;
    TextView chekin, checkout;

    boolean frag_alive = true;

    Context context;
    LinearLayout info_ll;
    TextView info_text;




    public MapFrag() {
        // Required empty public constructor
    }

    public static MapFrag newInstance(String param1, String param2) {
        MapFrag fragment = new MapFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MapFrag newInstance() {
        MapFrag fragment = new MapFrag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map_, container, false);

            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            chekin = view.findViewById(R.id.checkin);
            checkout = view.findViewById(R.id.checkout);
            info_ll = view.findViewById(R.id.info_ll);
            info_text = view.findViewById(R.id.info_text);

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    //mMap.getUiSettings().setZoomControlsEnabled(true);
                    //mMap.getUiSettings().setZoomGesturesEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(false);
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                    mMap.getUiSettings().setAllGesturesEnabled(false);


                }
            });

            CheckGpsConnection();

            chekin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckIn(locationAddres, Double.toString(latitude), Double.toString(longitude), "1");
                }
            });

            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckOut(locationAddres, Double.toString(latitude), Double.toString(longitude), "2", PreferenceUtil.getData("checkin_id", context));
                }
            });
        }


        return view;
    }

    private void CheckIn(final String name, String latitude, String longitude, String type) {

        Utils.displayCustomDailog(getActivity());

        Map<String, String> headers = new HashMap<>();
        String credentials = PreferenceUtil.getData("username", getActivity()) + ":" + PreferenceUtil.getData("password", getActivity());
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("location", name);
        jsonParams.put("lat", latitude);
        jsonParams.put("long", longitude);
        jsonParams.put("log_type", type);

        Log.d("RespondedData", jsonParams.toString() + " headers: \n" + headers);


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Utils.CheckinApi, jsonParams, headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResponseS", response.toString());
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")) {

                                Utils.dismissCustomDailog();

                                if (!response.getString("data").equals("null"))//sucess
                                {


                                    String msg = response.getJSONObject("messages").getJSONArray("success").get(0).toString();
                                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                                    PreferenceUtil.saveData("checkin_id", response.getString("data"), context);

                                    chekin.setVisibility(View.GONE);
                                    checkout.setVisibility(View.VISIBLE);

                                    info_text.setText("" + name);
                                    info_ll.setVisibility(View.VISIBLE);

                                } else {

                                    String msg = response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Utils.dismissCustomDailog();
                                Toast.makeText(getActivity(), "" + response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Utils.dismissCustomDailog();
                            Toast.makeText(getActivity(), "Json Error:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ResponseE", error.toString());

                        Utils.dismissCustomDailog();
                        Toast.makeText(getActivity(), "Volley Error:\n" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(customRequest);

    }

    private void CheckOut(String name, String latitude, String longitude, String type, String link) {

        Utils.displayCustomDailog(getActivity());

        Map<String, String> headers = new HashMap<>();
        String credentials = PreferenceUtil.getData("username", getActivity()) + ":" + PreferenceUtil.getData("password", getActivity());
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);


        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("location", name);
        jsonParams.put("lat", latitude);
        jsonParams.put("long", longitude);
        jsonParams.put("log_type", type);
        jsonParams.put("in_link", link);

        Log.d("RespondedData", jsonParams.toString());


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Utils.CheckOutApi, jsonParams, headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResponseS", response.toString());
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")) {

                                Utils.dismissCustomDailog();

                                if (!response.getString("data").equals("null"))//sucess
                                {


                                    String msg = response.getJSONObject("messages").getJSONArray("success").get(0).toString();
                                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                                    chekin.setVisibility(View.VISIBLE);
                                    checkout.setVisibility(View.GONE);

                                    info_ll.setVisibility(View.GONE);

                                } else {

                                    String msg = response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Utils.dismissCustomDailog();
                                Toast.makeText(getActivity(), "" + response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Utils.dismissCustomDailog();
                            Toast.makeText(getActivity(), "Json Error:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ResponseE", error.toString());

                        Utils.dismissCustomDailog();
                        Toast.makeText(getActivity(), "Volley Error:\n" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(customRequest);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CheckGpsConnection() {

        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Utils.displayCustomDailog(context);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (!hasGPSDevice(context)) {
                Toast.makeText(getActivity(), "Gps not Supported", Toast.LENGTH_SHORT).show();
            }

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(context)) {

               enableLoc();
               //EnableLocation();
            } else {
                //enabled

                getLatandLon();
            }


        } else {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


    }


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            Log.d("ResponseLoc","connected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                            Log.d("ResponseLoc","connectedSuspended");
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

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

            PendingResult<LocationSettingsResult> result =LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());



            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResult(LocationSettingsResult result) {

                    final Status status = result.getStatus();

                    switch (status.getStatusCode()) {

                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.

                            Log.d("ResponseLoc","responsesucces");

                            getLatandLon();


                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.d("ResponseLoc","response_resolutionrequired");
                            try {

                               // status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                                startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_LOCATION, null, 0, 0, 0, null);

                            } catch (IntentSender.SendIntentException e) {
                                Utils.dismissCustomDailog();
                                // Ignore the error.
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            Log.d("ResponseLoc","responsecheckunavailable");
                            Utils.dismissCustomDailog();
                            break;
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        getLatandLon();

                        break;
                    case Activity.RESULT_CANCELED:

                        Utils.dismissCustomDailog();

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, REQUEST_ENABLE_GPS);

                        break;
                    default:
                        Utils.dismissCustomDailog();
                        Log.d("Response","default activity result");
                        break;
                }
                break;

            case REQUEST_ENABLE_GPS:

                getLatandLon();

                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void getLatandLon() {

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.



                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.d("ResponseLoc", latitude + " " + longitude);

                if (frag_alive) {
                    getLocationDetails(location);
                }


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


            CheckGpsConnection();

        } else {


            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Permission Denied", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getLocationDetails(Location location) {

        // Utils.displayCustomDailog(getActivity());

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {




               /* String s = "Address Line: "
                        + addresses.get(0).getAddressLine(0) + "\n"
                        + addresses.get(0).getFeatureName() + "\n"
                        + "Locality: "
                        + addresses.get(0).getLocality() + "\n"
                        + addresses.get(0).getPremises() + "\n"
                        + "Admin Area: "
                        + addresses.get(0).getAdminArea() + "\n"
                        + "Country code: "
                        + addresses.get(0).getCountryCode() + "\n"
                        + "Country name: "
                        + addresses.get(0).getCountryName() + "\n"
                        + "Phone: " + addresses.get(0).getPhone()
                        + "\n" + "Postbox: "
                        + addresses.get(0).getPostalCode() + "\n"
                        + "SubLocality: "
                        + addresses.get(0).getSubLocality() + "\n"
                        + "SubAdminArea: "
                        + addresses.get(0).getSubAdminArea() + "\n"
                        + "SubThoroughfare: "
                        + addresses.get(0).getSubThoroughfare()
                        + "\n" + "Thoroughfare: "
                        + addresses.get(0).getThoroughfare() + "\n"
                        + "URL: " + addresses.get(0).getUrl();*/


                Utils.dismissCustomDailog();

                locationAddres = addresses.get(0).getAddressLine(0);
                Log.d("Response",locationAddres);

                if (mMap != null) {
                    mMap.clear();

                    LatLng curentloc = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(curentloc).title("Current location").snippet(locationAddres));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curentloc, 15));
                    // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(curentloc, 10, 30, 0)));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


                }


            }

            Utils.dismissCustomDailog();

        } catch (IOException e) {

            Utils.dismissCustomDailog();
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        frag_alive = hidden;
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(context);
        this.context = ctx;
    }


    void EnableLocation()
    {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);


         LocationServices.getSettingsClient(context)
                 .checkLocationSettings(builder.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                        Log.e("Response","Sucess");
                        getLatandLon();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        Log.e("Response","Failed Status code:"+statusCode);
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:




                               try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("Response","Unable to execute request.");
                                    Utils.dismissCustomDailog();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Utils.dismissCustomDailog();
                                Log.e("Response","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");

                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("Response","checkLocationSettings -> onCanceled");
                        Utils.dismissCustomDailog();
                    }
                });

    }

}
