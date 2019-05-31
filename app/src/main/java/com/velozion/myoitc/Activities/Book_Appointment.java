package com.velozion.myoitc.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.velozion.myoitc.Adapter.TimeSlotAdapter;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.db.BookingResponse;
import com.velozion.myoitc.databinding.ActivityBookAppointmentBinding;
import com.velozion.myoitc.db.DoctorProfileData;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Book_Appointment extends BaseActivity {


    Button booknow;
    RecyclerView time_recyclerview;

    String selected_date=null,selected_time=null;

    ArrayList<String> timing_slots=new ArrayList<>();

    TimeSlotAdapter timeSlotAdapter;
    DoctorProfileData doctorProfileData;

    ActivityBookAppointmentBinding bookAppointmentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookAppointmentBinding= DataBindingUtil.setContentView(this,R.layout.activity_book__appointment);

        BaseActivity.setActionBarTitle("Book Appointment");
        BaseActivity.enableHomeUpButton(true);

        doctorProfileData=(DoctorProfileData) getIntent().getExtras().getParcelable("data");
        bookAppointmentBinding.setDoctorProfile(doctorProfileData);


        booknow=(Button)findViewById(R.id.bn_book);

        time_recyclerview=(RecyclerView)findViewById(R.id.bn_timingrecyclerview);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),4);
        time_recyclerview.setLayoutManager(gridLayoutManager);






        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selected_time=timeSlotAdapter.getSelectedTimeSlot();

                if (selected_date==null)
                {
                    Snackbar.make(getWindow().getDecorView().getRootView(),"Select Date",Snackbar.LENGTH_LONG).show();
                }else if (selected_time==null)
                {

                    Snackbar.make(getWindow().getDecorView().getRootView(),"Select Time Slot",Snackbar.LENGTH_LONG).show();
                }else {

                    Utils.displayCustomDailog(Book_Appointment.this);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Utils.dismissCustomDailog();


                            BookingResponse bookingResponse=new BookingResponse("4561","Nagi","20-1-2019","2:00 PM");


                            Intent intent=new Intent(getApplicationContext(),BookingStatus.class);
                            intent.putExtra("data",doctorProfileData);
                            intent.putExtra("bookingdata",bookingResponse);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                }




            }
        });

        HandleCalender();

        try {
            HandleTimeSlots();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void HandleTimeSlots() throws ParseException {

        //9:00 AM - 7:00 PM

        String slot=doctorProfileData.getAvaliable_timings();

        String[] dates=slot.split("-");

        String fromTime = dates[0];
        String toTime = dates[1];
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Date dStart = df.parse(fromTime);
        Date dEnd = df.parse(toTime);


        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(dStart);

        int hour = calendarStart.get(Calendar.HOUR_OF_DAY);
        int minute = calendarStart.get(Calendar.MINUTE);
        String curTime = String.format("%02d:%02d", hour, minute); //convert to 0 format ex 09:03
        timing_slots.add(curTime);


        while(calendarStart.getTime().before(dEnd)){

            calendarStart.add(Calendar.MINUTE, 30);
            //String time=calendarStart.get(Calendar.HOUR_OF_DAY)+":"+calendarStart.get(Calendar.MINUTE);
              hour = calendarStart.get(Calendar.HOUR_OF_DAY);

             curTime = String.format("%02d:%02d", hour, minute);

             timing_slots.add(curTime);

        }


        if (timing_slots.size()>0)
        {

            timeSlotAdapter=new TimeSlotAdapter(getApplicationContext(),timing_slots);
            time_recyclerview.setAdapter(timeSlotAdapter);

        }



    }



    private void HandleCalender() {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(8)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                int day=date.get(Calendar.DATE)+1;

                if (day==32)
                {
                    day=1;
                }

                selected_date=(day+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR));
                Log.d("Response",selected_date);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });
    }


}
