package com.velozion.myoitc.Activities;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.Adapter.DocterCatAdapter;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsCategory extends BaseActivity {

    RecyclerView recyclerView;

    String[] catlist = new String[]{"Women's Health", "Skin & Hair", "Child Specialist", "General Physician", "Dental Care", "Ear Nose Threat", "Homoeopathy", "Bone ad Joints",
            "Sex Specialist", "Eye Specialist", "Digestive Issues", "Mental Wellness", "Physiotherapy", "Diabetes Management", "Brain and Nerves", "Kidney and Urinary Issues", "Ayurveda", "General Surgery", "Lungs and Breathing", "Heart", "Cancer"};


    int[] images = new int[]{R.drawable.dc_womenhealth, R.drawable.dc_skin_hair, R.drawable.dc_child, R.drawable.dc_general, R.drawable.dc_dental, R.drawable.dc_ears, R.drawable.dc_homeophaty, R.drawable.dc_bones,
            R.drawable.dc_sex, R.drawable.dc_eye, R.drawable.dc_digestive, R.drawable.dc_mental, R.drawable.dc_physiotheraphy, R.drawable.dc_diabities, R.drawable.dc_brain, R.drawable.dc_keydney, R.drawable.dc_ayurvedic, R.drawable.dc_general, R.drawable.dc_lungs, R.drawable.dc_heart, R.drawable.dc_cancer};

    ArrayList<HashMap<String, String>> Data = new ArrayList<>();
    DocterCatAdapter docterCatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setToolbarTitle(getResources().getString(R.string.activity_doctor_cat));
        setContentView(R.layout.activity_doctors_category);

        recyclerView = (RecyclerView) findViewById(R.id.doc_cat_recylerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        LoadCategories();

    }

    private void LoadCategories() {

        for (int i = 0; i < catlist.length; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", String.valueOf((i + 1)));
            hashMap.put("name", catlist[i]);
            Data.add(hashMap);

        }

        if (Data.size() > 0) {
            docterCatAdapter = new DocterCatAdapter(getApplicationContext(), Data, images);
            recyclerView.setAdapter(docterCatAdapter);

        }
    }



}
