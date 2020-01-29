package com.velozion.myoitc.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.badoualy.stepperindicator.StepperIndicator;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.adapter.ClientRequestServiceAdapter;
import com.velozion.myoitc.fragments.CustomerDemographicsFrag;
import com.velozion.myoitc.fragments.CustomerOtherInformationFrag;
import com.velozion.myoitc.fragments.CustomerRequestServiceFrag;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ClientRequestServicesActivity extends BaseActivity implements CustomerDemographicsFrag.MyListener, CustomerRequestServiceFrag.MyListener, CustomerOtherInformationFrag.MyListener {

    public ClientRequestServiceAdapter clientRequestServiceAdapter;
    private StepperIndicator indicator;
    private Button btn_Next, btn_previous;

    boolean demoIsChecking = false;
    HashMap<String, String> demoHashMap;

    boolean requestIsChecking = false;
    HashMap<String, String> requestHashMap;

    boolean otherInfoIsChecking = false;
    HashMap<String, String> otherInfoHashMap;

    HashMap<String, String> hmap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( " Referral for Service Form" );
        setContentView( R.layout.activity_client_request_services );
        indicator = findViewById( R.id.customer_formindicator );
        btn_Next = findViewById( R.id.customer_btnNext );
        btn_previous = findViewById( R.id.customer_btnprevious );


        initializePaging();


    }

    private void initializePaging() {
        List<Fragment> fragments = new Vector<>();

        CustomerDemographicsFrag customerDemographics = new CustomerDemographicsFrag( ClientRequestServicesActivity.this, this );
        fragments.add( customerDemographics );

        CustomerRequestServiceFrag customerRequestServiceFrag = new CustomerRequestServiceFrag( ClientRequestServicesActivity.this, this );
        fragments.add( customerRequestServiceFrag );

        CustomerOtherInformationFrag customerOtherInformationFrag = new CustomerOtherInformationFrag( ClientRequestServicesActivity.this, this );
        fragments.add( customerOtherInformationFrag );

        clientRequestServiceAdapter = new ClientRequestServiceAdapter( super.getSupportFragmentManager(), fragments ) {
        };
        ViewPager pager = super.findViewById( R.id.customer_viewpager );
        pager.setAdapter( clientRequestServiceAdapter );
        pager.beginFakeDrag();

        indicator.setViewPager( pager );

        btn_Next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() == 0) {
                    customerDemographics.getDemographicData();
                    if (demoIsChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                }
                if (pager.getCurrentItem() == 1) {
                    customerRequestServiceFrag.getCustomerRequestData();
                    if (requestIsChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }

                }
                if (pager.getCurrentItem() == 2) {
                    customerOtherInformationFrag.getOtherInfoData();
                    if (otherInfoIsChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                }
            }
        } );

        btn_previous.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem( pager.getCurrentItem() - 1, true );
                demoIsChecking = false;
                requestIsChecking = false;
                otherInfoIsChecking = false;
            }
        } );

        pager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pager.getCurrentItem() == 0) {
                    btn_previous.setVisibility( View.INVISIBLE );
                } else if (pager.getCurrentItem() == 2) {
                    btn_Next.setText( "Submit" );

                } else {
                    if (pager.getCurrentItem() != 0) {
                        btn_previous.setVisibility( View.VISIBLE );
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        } );
        if (pager.getCurrentItem() == 0) {
            btn_previous.setVisibility( View.INVISIBLE );
        }
    }

    @Override
    public void demographicCallBack(boolean isDemoChecking, HashMap<String, String> hashMap_demographic) {
        demoIsChecking = isDemoChecking;
        demoHashMap = hashMap_demographic;

        hmap.putAll( demoHashMap );

    }

    @Override
    public void customerRequestCallBack(boolean isRequestChecking, HashMap<String, String> hashMap_customerRequest) {
        requestIsChecking = isRequestChecking;
        requestHashMap = hashMap_customerRequest;
        hmap.putAll( requestHashMap );
    }

    @Override
    public void otherInfoCallBack(boolean otherInfoChecking, HashMap<String, String> hashMap_otherInfo) {
        otherInfoIsChecking = otherInfoChecking;
        otherInfoHashMap = hashMap_otherInfo;
        hmap.putAll( otherInfoHashMap );
        Toast.makeText( this, "" + hmap, Toast.LENGTH_SHORT ).show();
    }


}
