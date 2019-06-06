package com.velozion.myoitc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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


        holder.pic.setImageResource(images[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorsList_Activity.class);
                intent.putExtra("id", data.get(position).get("id"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


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
}
