package com.velozion.myoitc.Activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.db.BookingResponse;
import com.velozion.myoitc.db.DoctorProfileData;
import com.velozion.myoitc.R;
import com.velozion.myoitc.databinding.ActivityBookingStatusBinding;

public class BookingStatus extends BaseActivity {



    TextView back;

    ActivityBookingStatusBinding bookingStatusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookingStatusBinding=DataBindingUtil.setContentView(this,R.layout.activity_booking_status);

        BaseActivity.disableActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_light));
        }

        DoctorProfileData doctorProfileData=getIntent().getExtras().getParcelable("data");
        BookingResponse bookingResponse=getIntent().getExtras().getParcelable("bookingdata");


        bookingStatusBinding.setDoctorProfile(doctorProfileData);
        bookingStatusBinding.setBookingResponse(bookingResponse);


        back=(TextView) findViewById(R.id.bn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
