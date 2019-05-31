package com.velozion.myoitc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.velozion.myoitc.Adapter.DocterCatAdapter;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsCategory extends BaseActivity {

    RecyclerView recyclerView;

    String[] catlist=new String[]{"Women's Health","Skin & Hair","Child Specialist","general Physician","Dental Care","Ear Nose Threat","Homoeopathy","Bone adn Joints",
                                    "Sex Specialist","Eye Specialist","Digestive Issues","Mental Wellness"};


    ArrayList<HashMap<String,String>> Data=new ArrayList<>();
    DocterCatAdapter docterCatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_category);

        BaseActivity.setActionBarTitle("Search Docters");
        BaseActivity.enableHomeUpButton(true);

        recyclerView=(RecyclerView)findViewById(R.id.doc_cat_recylerview);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        LoadCategories();

    }

    private void LoadCategories() {

        for (int i=0;i<catlist.length;i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("id", String.valueOf((i+1)));
            hashMap.put("name",catlist[i]);
            hashMap.put("pic","");
            Data.add(hashMap);

        }

        if (Data.size()>0)
        {
            docterCatAdapter=new DocterCatAdapter(getApplicationContext(),Data);
            recyclerView.setAdapter(docterCatAdapter);

        }
    }


}
