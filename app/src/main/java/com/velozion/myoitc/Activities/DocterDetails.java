package com.velozion.myoitc.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.databinding.DoctorProfileLayoutBinding;
import com.velozion.myoitc.db.DoctorProfileData;

public class DocterDetails extends BaseActivity {

    DoctorProfileLayoutBinding profileLayoutBinding;
    DoctorProfileData doctorProfileData;
    ImageView staticmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileLayoutBinding = DataBindingUtil.setContentView(this, R.layout.doctor_profile_layout);

        doctorProfileData = getIntent().getExtras().getParcelable("data");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_light));
        }

        profileLayoutBinding.setDoctorProfile(doctorProfileData);
        staticmap = (ImageView) findViewById(R.id.pd_hospotal_loc_pic);

        profileLayoutBinding.pdBookappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookAppointment.class);
                intent.putExtra("data", doctorProfileData);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        profileLayoutBinding.pdPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DisplayFullImage.class);
                intent.putExtra("profile_name",doctorProfileData.getName());
                intent.putExtra("profile_pic",doctorProfileData.getPic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        String data = "https://maps.googleapis.com/maps/api/staticmap?&zoom=14&size=330x130&maptype=roadmap&markers=color:green%7Clabel:G%7C" + doctorProfileData.getLat() + "," + doctorProfileData.getLang() + "-38.401606&key=AIzaSyAWSNJ2-Kdk2aNtM1aqJpaLGhtL3ZZpCMk";
        Utils.ImageLoaderInitialization(getApplicationContext());
        Utils.LoadImage(data, staticmap);
    }
}
