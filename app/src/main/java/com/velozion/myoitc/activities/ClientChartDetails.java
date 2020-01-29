package com.velozion.myoitc.activities;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.adapter.ClientChartDetailsAdapter;
import com.velozion.myoitc.bean.ClientChartBean;
import com.velozion.myoitc.bean.ClientChartTaskListBean;
import com.velozion.myoitc.databinding.ActivityClientChartDetailsBinding;

import java.util.ArrayList;

public class ClientChartDetails extends BaseActivity {

    ClientChartBean clientChartBeans;
    ArrayList<ClientChartTaskListBean> taskListBeans = new ArrayList<>();
    ActivityClientChartDetailsBinding activityClientChartDetailsBinding;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setHomeMenuRequired(false);

        activityClientChartDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_client_chart_details);

        Bundle data = this.getIntent().getExtras();
        if (data != null) {
            clientChartBeans = data.getParcelable("client_chart_array_list");
            taskListBeans = data.getParcelableArrayList("client_chart_task_array_list");

            setToolbarTitle(clientChartBeans.getClientName());
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        activityClientChartDetailsBinding.recyclerclientcharprofile.setLayoutManager(linearLayoutManager);

        activityClientChartDetailsBinding.setClientChartBean(clientChartBeans);
        Utils.ImageLoaderInitialization(this);
        Utils.LoadImage(clientChartBeans.getCliImage(), activityClientChartDetailsBinding.clntprfimage);
        setUpAdapter();
    }


    private void setUpAdapter() {
        ClientChartDetailsAdapter client_chartProfileAdapter = new ClientChartDetailsAdapter(ClientChartDetails.this, taskListBeans);
        activityClientChartDetailsBinding.recyclerclientcharprofile.setAdapter(client_chartProfileAdapter);
    }
}
