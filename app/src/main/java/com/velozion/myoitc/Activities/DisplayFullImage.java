package com.velozion.myoitc.Activities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

public class DisplayFullImage extends BaseActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarRequired(true);
        setToolbarTitle(getIntent().getStringExtra("profile_name"));
        setContentView(R.layout.activity_display_full_image);


        try {
            getToolbar().setBackgroundColor(getResources().getColor(R.color.black));
            changeStatusbar(getResources().getColor(R.color.black));
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /*
         intent.putExtra("profile_name",data.get(position).getName());
                intent.putExtra("profile_pic",data.get(position).getPic());
         */


        imageView=(ImageView)findViewById(R.id.fullimagview);


        Utils.ImageLoaderInitialization(getApplicationContext());
        Utils.LoadImage(getIntent().getStringExtra("profile_pic"),imageView);

    }

    void changeStatusbar(int color)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
