package com.velozion.myoitc.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.bean.CheckInHistoryBean;
import com.velozion.myoitc.databinding.ActivityHistoryDetailsBinding;

import java.util.ArrayList;

public class HistoryDetails extends BaseActivity {

    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    ActivityHistoryDetailsBinding historyDetailsBinding;
    CheckInHistoryBean checkInHistoryBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setToolbarTitle( "History Details" );
        historyDetailsBinding = DataBindingUtil.setContentView( this, R.layout.activity_history_details );

        checkInHistoryBean = getIntent().getExtras().getParcelable( "data" );
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );
        historyDetailsBinding.setHistory( checkInHistoryBean );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
            window.setStatusBarColor( getResources().getColor( R.color.colorPrimaryDark ) );
        }
        supportMapFragment.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
                //mMap.getUiSettings().setScrollGesturesEnabled(false);
                //mMap.getUiSettings().setAllGesturesEnabled(false);
                mMap.getUiSettings().setCompassEnabled( false );
                mMap.getUiSettings().setMapToolbarEnabled( false );
                mMap.getUiSettings().setZoomControlsEnabled( true );
                mMap.getUiSettings().setZoomGesturesEnabled( true );

                String serverKey = getResources().getString( R.string.google_key );
                LatLng origin = new LatLng( 13.012110, 77.674020 );
                LatLng destination = new LatLng( 13.012200, 77.665750 );
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
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline( HistoryDetails.this, directionPositionList, 5, Color.RED );
                                    mMap.addPolyline( polylineOptions );
                                    float zoomLevel = (float) 15.0;
                                    mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( origin, zoomLevel ) );
                                    mMap.addMarker( new MarkerOptions().position( origin ) );
                                    mMap.addMarker( new MarkerOptions().position( destination ) );
                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                            }
                        } );
            }
        } );
    }
}


