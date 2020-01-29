package com.velozion.myoitc.activities;

import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.bean.TravelMapBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TravelMapActivity extends BaseActivity {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    List<TravelMapBean> travelMapBeans;
    ArrayList<String> clientNames;
    Spinner spinnerfrom, spinnerto;
    String clintFrom, clientTo;
    Button btnDirections;
    double starLatitude, startlongitude, endLatitude, endLongitude;
    PolylineOptions polylineOptions;
    LocationManager locationManager;
    Marker marker = null;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Travel Map" );
        setContentView( R.layout.activity_travel_map );

        btnDirections = findViewById( R.id.iddirections );
        spinnerfrom = findViewById( R.id.spinnerFrom );
        spinnerto = findViewById( R.id.spinnerto );
        locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );


        getNames();
        btnDirections.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverKey = getResources().getString( R.string.google_key );
                LatLng origin = new LatLng( starLatitude, startlongitude );
                LatLng destination = new LatLng( endLatitude, endLongitude );
                GoogleDirection.withServerKey( serverKey )
                        .from( origin )
                        .to( destination )
                        .transitMode( TransportMode.DRIVING )
                        .language( Language.ENGLISH )
                        .unit( Unit.METRIC )
                        .avoid( AvoidType.FERRIES )
                        .avoid( AvoidType.INDOOR )
                        .alternativeRoute( true )
                        .execute( new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction) {
                                String status = direction.getStatus();
                                if (status.equals( RequestResult.OK )) {
                                    Route route = direction.getRouteList().get( 0 );
                                    Leg leg = route.getLegList().get( 0 );
                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                                    if (marker != null) {
                                        mMap.clear();
                                    }
                                    marker = mMap.addMarker( new MarkerOptions().position( origin ) );
                                    marker = mMap.addMarker( new MarkerOptions().position( destination ) );

                                    polylineOptions = DirectionConverter.createPolyline( TravelMapActivity.this, directionPositionList, 5, Color.RED );
                                    mMap.addPolyline( polylineOptions );
                                    LatLng latLng = new LatLng( starLatitude, startlongitude );
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( latLng, 16 );
                                    mMap.animateCamera( cameraUpdate );
                                    Toast.makeText( TravelMapActivity.this, "" + origin + destination, Toast.LENGTH_SHORT ).show();
                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                            }
                        } );
            }
        } );

        mapFragment.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
                mMap.getUiSettings().setCompassEnabled( false );
                mMap.getUiSettings().setMapToolbarEnabled( false );
                mMap.getUiSettings().setZoomControlsEnabled( true );
                mMap.getUiSettings().setZoomGesturesEnabled( true );
            }
        } );
    }

    public void getNames() {
        StringRequest stringRequest = new StringRequest( Request.Method.GET, Utils.travelMapAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobj = new JSONObject( response );
                    if (response != null) {
                        travelMapBeans = new ArrayList<>();
                        clientNames = new ArrayList<>();
                        JSONArray jsonArray = mainobj.getJSONArray( "details" );
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject( i );
                            travelMapBeans.add( new TravelMapBean( jsonObject.getString( "clientName" ),
                                    jsonObject.getDouble( "latitude" ),
                                    jsonObject.getDouble( "longitude" ) ) );
                            clientNames.add( jsonObject.getString( "clientName" ) );
                        }
                        setSpinnerNames();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } );

        RequestQueue requestQueue = Volley.newRequestQueue( this );
        requestQueue.add( stringRequest );
    }

    public void setSpinnerNames() {
        spinnerfrom.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clintFrom = spinnerfrom.getSelectedItem().toString();
                starLatitude = travelMapBeans.get( i ).getLatitude();
                startlongitude = travelMapBeans.get( i ).getLongitude();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );

        spinnerto.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clientTo = spinnerto.getSelectedItem().toString();
                endLatitude = travelMapBeans.get( i ).getLatitude();
                endLongitude = travelMapBeans.get( i ).getLongitude();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        } );
        ArrayAdapter<String> adapterNames = new ArrayAdapter<String>( this,
                android.R.layout.simple_spinner_item, clientNames );
        adapterNames.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
        spinnerfrom.setAdapter( adapterNames );
        spinnerto.setAdapter( adapterNames );
    }
}


