package com.velozion.myoitc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.databinding.DoctorProfileLayoutBinding;
import com.velozion.myoitc.db.DoctorProfileData;

public class DocterDetails extends BaseActivity {

    DoctorProfileLayoutBinding profileLayoutBinding;
    DoctorProfileData doctorProfileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MorningSession);
        super.onCreate(savedInstanceState);

        profileLayoutBinding = DataBindingUtil.setContentView(this, R.layout.doctor_profile_layout);

        doctorProfileData = getIntent().getExtras().getParcelable("data");
        profileLayoutBinding.setDoctorProfile(doctorProfileData);

        profileLayoutBinding.pdBookappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookAppointment.class);
                intent.putExtra("data", doctorProfileData);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        profileLayoutBinding.pdPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayFullImage.class);
                intent.putExtra("profile_name", doctorProfileData.getName());
                intent.putExtra("profile_pic", doctorProfileData.getPic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        profileLayoutBinding.pdHospotalLocPic.onCreate(savedInstanceState);
        profileLayoutBinding.pdHospotalLocPic.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

               /* googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.getUiSettings().setAllGesturesEnabled(false);*/




                LatLng curentloc = new LatLng(Double.parseDouble(doctorProfileData.getLat()), Double.parseDouble(doctorProfileData.getLang()));
                googleMap.addMarker(new MarkerOptions().position(curentloc));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curentloc, 15));
               /* googleMap.addMarker(new MarkerOptions().position(curentloc));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curentloc, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);*/


            }
        });

       /* String data = "https://maps.googleapis.com/maps/api/staticmap?&zoom=14&size=330x130&maptype=roadmap&markers=color:green%7Clabel:G%7C" + doctorProfileData.getLat() + "," + doctorProfileData.getLang() + "-38.401606&key=AIzaSyAWSNJ2-Kdk2aNtM1aqJpaLGhtL3ZZpCMk";
        Utils.ImageLoaderInitialization(getApplicationContext());
        Utils.LoadImage(data, staticmap);*/
    }


    @Override
    public void onResume() {
        profileLayoutBinding.pdHospotalLocPic.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        profileLayoutBinding.pdHospotalLocPic.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileLayoutBinding.pdHospotalLocPic.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        profileLayoutBinding.pdHospotalLocPic.onLowMemory();
    }
}
