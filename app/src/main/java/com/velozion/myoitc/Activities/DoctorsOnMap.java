package com.velozion.myoitc.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.ViewModel.MyViewModel;
import com.velozion.myoitc.db.DoctorProfileData;

import java.util.ArrayList;

public class DoctorsOnMap extends BaseActivity {

    SupportMapFragment supportMapFragment;
    ProgressBar progressBar;
    private GoogleMap mMap;

    MyViewModel myViewModel;
    ArrayList<DoctorProfileData> List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CustomeTheme4);
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setToolbarTitle(getResources().getString(R.string.activity_doctors_list));
        setContentView(R.layout.activity_doctors_on_map);

        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        progressBar = (ProgressBar) findViewById(R.id.maps_progressbar);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);

                LoadDoctors();


            }
        });

        setToolbarRequired(true);
        setToolbarTitle(getResources().getString(R.string.activity_doctors_list));

    }

    private void LoadDoctors() {

        myViewModel.getDoctersList(this).observe(this, new Observer<ArrayList<DoctorProfileData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DoctorProfileData> doctorProfileData) {

                progressBar.setVisibility(View.GONE);

                if (doctorProfileData.size() > 0) {
                    List.addAll(doctorProfileData);
                    LatLng loc = null;

                    for (int i = 0; i < List.size(); i++) {

                        loc = new LatLng(Double.parseDouble(List.get(i).getLat()), Double.parseDouble(List.get(i).getLang()));
                        Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title(List.get(i).getName()).snippet(List.get(i).getSpecialist()));

                        List.get(i).setMobile(marker.getId());

                    }


                    CustominfoWindowAdapter custominfoWindowAdapter = new CustominfoWindowAdapter(getApplicationContext());
                    mMap.setInfoWindowAdapter(custominfoWindowAdapter);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                } else {

                    showAlertDialog();
                }

            }
        });

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorsOnMap.this);
        builder.setTitle("Myoitc");
        builder.setIcon(R.mipmap.applogo);
        builder.setMessage("No Doctors Found");
        builder.setCancelable(true);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    class CustominfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


        Context context;

        public CustominfoWindowAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getInfoWindow(Marker marker) {


            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {


            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_infowindow, null);


            ImageView image = (ImageView) view.findViewById(R.id.docter_image);

            TextView name = (TextView) view.findViewById(R.id.docter_name);
            TextView qualification = (TextView) view.findViewById(R.id.docter_education);
            TextView specalist = (TextView) view.findViewById(R.id.docter_specilist);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.docter_rating);


            if (marker.getId() != null) {
                for (int i = 0; i < List.size(); i++) {

                    if (List.get(i).getMobile().equals(marker.getId())) {


                        Utils.ImageLoaderInitialization(getApplicationContext());
                        Utils.LoadImage(List.get(i).getPic(), image);

                        name.setText("" + List.get(i).getName());
                        qualification.setText("" + List.get(i).getQualification());
                        specalist.setText("" + List.get(i).getSpecialist());
                        ratingBar.setRating(Float.parseFloat(List.get(i).getRating()));


                    }


                }
            }

            return view;
        }


    }
}
