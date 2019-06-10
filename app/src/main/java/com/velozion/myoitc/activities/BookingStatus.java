package com.velozion.myoitc.activities;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.databinding.ActivityBookingStatusBinding;
import com.velozion.myoitc.db.BookingResponse;
import com.velozion.myoitc.db.DoctorProfileData;

public class BookingStatus extends BaseActivity {

    ActivityBookingStatusBinding bookingStatusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.EveningSession);
        super.onCreate(savedInstanceState);

        bookingStatusBinding = DataBindingUtil.setContentView(this, R.layout.activity_booking_status);

        setToolbarRequired(true);


        DoctorProfileData doctorProfileData = getIntent().getExtras().getParcelable("data");
        BookingResponse bookingResponse = getIntent().getExtras().getParcelable("bookingdata");

        bookingStatusBinding.setDoctorProfile(doctorProfileData);
        bookingStatusBinding.setBookingResponse(bookingResponse);

        bookingStatusBinding.bnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                activateSlideRight();
            }
        });

    }


}
