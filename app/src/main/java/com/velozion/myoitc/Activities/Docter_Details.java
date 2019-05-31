package com.velozion.myoitc.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.db.DoctorProfileData;
import com.velozion.myoitc.R;
import com.velozion.myoitc.databinding.DoctorProfileLayoutBinding;

public class Docter_Details extends BaseActivity {

    RatingBar ratingBar;
    TextView booknow;
    DoctorProfileLayoutBinding profileLayoutBinding;
    DoctorProfileData doctorProfileData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileLayoutBinding=DataBindingUtil.setContentView(this,R.layout.doctor_profile_layout);
        doctorProfileData=getIntent().getExtras().getParcelable("data");

        BaseActivity.disableActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_light));
        }


        profileLayoutBinding.setDoctorProfile(doctorProfileData);

        ratingBar=(RatingBar)findViewById(R.id.pd_rating);
        booknow=(TextView)findViewById(R.id.pd_bookappointment);

        ratingBar.setRating(Float.parseFloat(doctorProfileData.getRating()));

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Book_Appointment.class);
                intent.putExtra("data",doctorProfileData);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });





    }

    public class MyClickHandlers
    {

        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void onBookClick(View view) {

            Intent intent=new Intent(getApplicationContext(), Book_Appointment.class);
            intent.putExtra("data",doctorProfileData);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }







}
