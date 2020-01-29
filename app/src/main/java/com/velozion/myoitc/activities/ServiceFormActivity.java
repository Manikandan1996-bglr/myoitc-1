package com.velozion.myoitc.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.badoualy.stepperindicator.StepperIndicator;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.adapter.ServiceFormAdapter;
import com.velozion.myoitc.fragments.DemographicsFrag;
import com.velozion.myoitc.fragments.InsuranceInformationFrag;
import com.velozion.myoitc.fragments.OtherInformationFrag;
import com.velozion.myoitc.fragments.ProviderReferralFormFrag;
import com.velozion.myoitc.fragments.ServiceDetailsFrag;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ServiceFormActivity extends BaseActivity implements ProviderReferralFormFrag.MyListener, DemographicsFrag.MyListener, InsuranceInformationFrag.MyListener, OtherInformationFrag.MyListener, ServiceDetailsFrag.MyListener {
    public ServiceFormAdapter serviceAdapter;
    private Button btn_Next, btn_previous;
    private StepperIndicator indicator;

    boolean provideIsChecking = false;
    HashMap<String, String> proverderHashMap;

    boolean demoIsChecking = false;
    HashMap<String, String> demoHashMap;

    boolean insuranceChecking = false;
    HashMap<String, String> insuranceHashmap;

    boolean otherInformationChecking = false;
    HashMap<String, String> otherInformationHashMap;

    boolean serviceDetailsChecking = false;
    HashMap<String, String> serviceHasMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setToolbarRequired( true );
        setHomeMenuRequired( false );
        setToolbarTitle( "Referral for Service Form" );
        setContentView( R.layout.activity_service_form );
        btn_Next = findViewById( R.id.btn_Next );
        btn_previous = findViewById( R.id.btn_previous );
        indicator = findViewById( R.id.indicator );
        initializePaging();
    }

    private void initializePaging() {
        List<Fragment> fragments = new Vector<>();
        ProviderReferralFormFrag providerReferralFormFrag = new ProviderReferralFormFrag( ServiceFormActivity.this, this );
        fragments.add( providerReferralFormFrag );

        DemographicsFrag demographicsFrag = new DemographicsFrag( ServiceFormActivity.this, this );
        fragments.add( demographicsFrag );

        InsuranceInformationFrag informationFrag = new InsuranceInformationFrag( ServiceFormActivity.this, this );
        fragments.add( informationFrag );

        OtherInformationFrag otherInformationFrag = new OtherInformationFrag( ServiceFormActivity.this, this );
        fragments.add( otherInformationFrag );

        ServiceDetailsFrag serviceDetailsFrag = new ServiceDetailsFrag( ServiceFormActivity.this, this );
        fragments.add( serviceDetailsFrag );

        serviceAdapter = new ServiceFormAdapter( super.getSupportFragmentManager(), fragments ) {
        };

        ViewPager pager = super.findViewById( R.id.view_pager );
        pager.setAdapter( serviceAdapter );
        pager.beginFakeDrag();

        indicator.setViewPager( pager );

        btn_Next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() == 0) {
                    providerReferralFormFrag.getProfiderFormData();
                    if (provideIsChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                } else if (pager.getCurrentItem() == 1) {
                    demographicsFrag.getdemographicData();
                    if (demoIsChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                } else if (pager.getCurrentItem() == 2) {
                    informationFrag.getInsuranceData();
                    if (insuranceChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                } else if (pager.getCurrentItem() == 3) {
                    otherInformationFrag.getOtherInformationData();
                    if (otherInformationChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                } else if (pager.getCurrentItem() == 4) {
                    serviceDetailsFrag.getServiceDetailsData();
                    if (serviceDetailsChecking) {
                        pager.setCurrentItem( pager.getCurrentItem() + 1, true );
                    }
                }
            }
        } );
        btn_previous.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem( pager.getCurrentItem() - 1, true );
                provideIsChecking = false;
                demoIsChecking = false;
                insuranceChecking = false;
                otherInformationChecking = false;
                serviceDetailsChecking = false;
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
                } else if (pager.getCurrentItem() == 4) {
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
    public void callback(boolean isCheckingProvider, HashMap<String, String> hashMap_provider) {
        provideIsChecking = isCheckingProvider;
        proverderHashMap = hashMap_provider;
    }

    public void demographicCallBack(boolean isCheckingDemographic, HashMap<String, String> hashMap_demographic) {
        demoIsChecking = isCheckingDemographic;
        demoHashMap = hashMap_demographic;
    }

    @Override
    public void insuranceCallBack(boolean isCheckingInsurance, HashMap<String, String> hashMap_insurance) {
        insuranceChecking = isCheckingInsurance;
        insuranceHashmap = hashMap_insurance;
    }

    @Override
    public void otherInformationCallBack(boolean isCheckingOtherInfo, HashMap<String, String> hashMap_otherInfo) {
        otherInformationChecking = isCheckingOtherInfo;
        otherInformationHashMap = hashMap_otherInfo;
    }

    @Override
    public void callBackServiceDetail(boolean isCheckingServiceDetail, HashMap<String, String> hashMap_serviceDetail) {
        serviceDetailsChecking = isCheckingServiceDetail;
        serviceHasMap = hashMap_serviceDetail;
    }
}