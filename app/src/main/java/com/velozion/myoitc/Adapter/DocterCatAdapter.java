package com.velozion.myoitc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.velozion.myoitc.Activities.DoctorsList_Activity;
import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DocterCatAdapter extends RecyclerView.Adapter<DocterCatAdapter.DoctorsCatViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> data;
    int images[];
    int pos = 0;

    DisplayImageOptions options;
    ImageLoaderConfiguration imgconfig;

    public DocterCatAdapter(Context context, ArrayList<HashMap<String, String>> data, int[] imgs) {
        this.context = context;
        this.data = data;
        images = imgs;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .showImageOnFail(R.drawable.icon_user)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.NONE)
                .build();

        imgconfig = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(imgconfig);

    }

    @NonNull
    @Override
    public DoctorsCatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_doc_cate, viewGroup, false);
        DoctorsCatViewHolder doctorsCatViewHolder = new DoctorsCatViewHolder(view);
        return doctorsCatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsCatViewHolder holder, final int position) {

        /*
            hashMap.put("id", String.valueOf((i+1)));
            hashMap.put("name",catlist[i]);
            hashMap.put("pic","");
         */

        holder.name.setText("" + data.get(position).get("name"));


      //  holder.pic.setImageResource(images[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorsList_Activity.class);
                intent.putExtra("id", data.get(position).get("id"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


       /* Resources.Theme theme=context.getResources().newTheme();
        final Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),images[position], theme);
        holder.pic.setImageDrawable(drawable);*/


       ManageSvg(holder.pic,position);


        if (position > pos) {

            AnimUtils.animate(holder.itemView, true);
        } else {
            AnimUtils.animate(holder.itemView, false);

        }
        pos = position;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DoctorsCatViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView pic;

        public DoctorsCatViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.cat_name);
            pic = (ImageView) itemView.findViewById(R.id.cat_pic);
        }
    }

    private void HandleTint(ImageView imageView) {

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.themeColorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;


        imageView.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

    }


    void ManageSvg(ImageView imageView,int pos)
    {

        int data=getSelectedTheme();

        Resources.Theme theme = context.getTheme();
        theme.applyStyle(data, true);

        final Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),images[pos], theme);
       imageView.setImageDrawable(drawable);

    }

    int getSelectedTheme()
    {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            return R.style.MorningSession;
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            return R.style.AfternoonSession;
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            return R.style.EveningSession;
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            return R.style.NightSession;
        }

        return R.style.DefaultSession;

    }



}
