package com.velozion.myoitc.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.velozion.myoitc.Adapter.DoctorsAdapter;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.db.DoctorProfileData;
import com.velozion.myoitc.R;
import com.velozion.myoitc.ViewModel.MyViewModel;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_);

       BaseActivity.setActionBarTitle("Doctors List");
       BaseActivity.enableHomeUpButton(true);

        myViewModel=ViewModelProviders.of(this).get(MyViewModel.class);

        recyclerView=(RecyclerView)findViewById(R.id.appointment_recylerview);
        progressBar=(ProgressBar)findViewById(R.id.appointment_progressbar);
        textView=(TextView)findViewById(R.id.appointment_textview);
        nodata=(LinearLayout)findViewById(R.id.appointment_nodata_ll);

        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);


        LoadDoctors();

    }

    private void LoadDoctors() {

        myViewModel.getDoctersList(this).observe(this, new Observer<ArrayList<DoctorProfileData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DoctorProfileData> doctorProfileData) {

                if (doctorProfileData.size()>0)
                {
                    doctorsAdapter=new DoctorsAdapter(getApplicationContext(),doctorProfileData);
                    recyclerView.setAdapter(doctorsAdapter);

                    TriggerUI(true);


                }else {

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

    void TriggerUI(boolean showui)
    {
        if (showui)//show recyclerview
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);

        }else {//show error
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_maps:


                Intent intent=new Intent(getApplicationContext(),DoctorsOnMap.class);
                startActivity(intent);


                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
