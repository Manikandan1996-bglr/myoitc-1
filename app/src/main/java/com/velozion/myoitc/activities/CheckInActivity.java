package com.velozion.myoitc.activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.viewModel.MyViewModel;

public class CheckInActivity extends BaseActivity {
    MyViewModel myViewModel;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Check In" );
        setContentView( R.layout.content_home );

        myViewModel = ViewModelProviders.of( this ).get( MyViewModel.class );
        bottomNavigationView = findViewById( R.id.bottomnavigationview );

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById( R.id.my_nav_host_fragment );
        NavigationUI.setupWithNavController( bottomNavigationView, navHostFragment.getNavController() );
    }
}
