package com.velozion.myoitc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.Adapter.DoctorsAdapter;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.ViewModel.MyViewModel;
import com.velozion.myoitc.db.DoctorProfileData;

import java.util.ArrayList;

public class DoctorsList_Activity extends BaseActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout nodata;
    TextView textView;

    MyViewModel myViewModel;
    DoctorsAdapter doctorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(R.style.CustomeTheme2);
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setToolbarTitle(getResources().getString(R.string.activity_doctors_list));
        setContentView(R.layout.activity_appointment_);

        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        recyclerView = findViewById(R.id.appointment_recylerview);
        progressBar = findViewById(R.id.appointment_progressbar);
        textView = findViewById(R.id.appointment_textview);
        nodata = findViewById(R.id.appointment_nodata_ll);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);

        LoadDoctors();

    }

    private void LoadDoctors() {

        myViewModel.getDoctersList(this).observe(this, new Observer<ArrayList<DoctorProfileData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DoctorProfileData> doctorProfileData) {

                if (doctorProfileData.size() > 0) {
                    doctorsAdapter = new DoctorsAdapter(DoctorsList_Activity.this, doctorProfileData);
                    recyclerView.setAdapter(doctorsAdapter);

                    TriggerUI(true);


                } else {

                    textView.setText("No Data Found");
                    TriggerUI(false);
                }

            }
        });


        myViewModel.Failuremessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                TriggerUI(false);

            }
        });

        myViewModel.jsonError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                TriggerUI(false);

            }
        });

        myViewModel.volleyError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                TriggerUI(false);

            }
        });

    }

    void TriggerUI(boolean showui) {
        if (showui)//show recyclerview
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);

        } else {//show error
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_maps:


                Intent intent = new Intent(getApplicationContext(), DoctorsOnMap.class);
                startActivity(intent);


                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
