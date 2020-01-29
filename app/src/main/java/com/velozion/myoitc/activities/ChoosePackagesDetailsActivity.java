package com.velozion.myoitc.activities;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.adapter.ChoosePackageDetailsAdapter;
import com.velozion.myoitc.bean.ChoosePackageBeans;

import java.util.ArrayList;

public class ChoosePackagesDetailsActivity extends BaseActivity {

    protected ArrayList<ChoosePackageBeans> packageBeanArrayList;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Choose Packages Details" );
        setContentView( R.layout.choose_packages_details_activity );

        ViewPager mPager = findViewById( R.id.pager );

        Bundle data = this.getIntent().getExtras();
        if (data != null) {
            packageBeanArrayList = data.getParcelableArrayList( "packageBeanList" );
            position = data.getInt( "position" );
        }
        mPager.setAdapter( new ChoosePackageDetailsAdapter( this, packageBeanArrayList ) );
        mPager.setCurrentItem( position );
    }
}
