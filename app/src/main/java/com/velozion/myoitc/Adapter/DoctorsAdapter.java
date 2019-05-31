package com.velozion.myoitc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.velozion.myoitc.Activities.Book_Appointment;
import com.velozion.myoitc.Activities.Docter_Details;
import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.db.DoctorProfileData;
import com.velozion.myoitc.R;

import java.util.ArrayList;

public class DoctorsAdapter extends RecyclerView.Adapter <DoctorsAdapter.DoctorsViewHolder>{

    Context context;
    ArrayList<DoctorProfileData> data;
    int pos=0;

    DisplayImageOptions options;
    ImageLoaderConfiguration imgconfig;

    public DoctorsAdapter(Context context, ArrayList<DoctorProfileData> data) {
        this.context = context;
        this.data = data;

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
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_docters,viewGroup,false);
       DoctorsViewHolder doctorsViewHolder=new DoctorsViewHolder(view);
        return doctorsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, final int position) {



        holder.name.setText(""+data.get(position).getName());
        holder.education.setText(""+data.get(position).getQualification());
        holder.specalist.setText(""+data.get(position).getSpecialist()+"  \u2022 "+data.get(position).getExperience());
        holder.ratingBar.setRating(Float.parseFloat(data.get(position).getRating()));

        holder.locality.setText(""+data.get(position).getHospital_name()+" , "+data.get(position).getArea());
        holder.fees.setText(" \u20B9 "+data.get(position).getFees());
        holder.mobile.setText(""+data.get(position).getMobile());

        ImageLoader.getInstance().displayImage(data.get(position).getPic(),holder.pic,options);


        holder.view_prfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, Docter_Details.class);
                intent.putExtra("data",data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, Book_Appointment.class);
                intent.putExtra("data",data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(position>pos)
        {

            AnimUtils.animate(holder.itemView,true);
        }else {
            AnimUtils.animate(holder.itemView,false);

        }
        pos=position;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DoctorsViewHolder extends RecyclerView.ViewHolder{

        TextView name,education,specalist;
        TextView view_prfile,book_appointment;
        TextView locality,fees,mobile;
        RatingBar ratingBar;
        ImageView pic;
        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.docter_name);
            education=(TextView)itemView.findViewById(R.id.docter_education);
            specalist=(TextView)itemView.findViewById(R.id.docter_specilist);

            view_prfile=(TextView)itemView.findViewById(R.id.docter_profile);
            book_appointment=(TextView)itemView.findViewById(R.id.docter_bookappointment);

            locality=(TextView)itemView.findViewById(R.id.docter_hospital);
            fees=(TextView)itemView.findViewById(R.id.docter_fees);
            mobile=(TextView)itemView.findViewById(R.id.docter_mobile);

            ratingBar=(RatingBar)itemView.findViewById(R.id.docter_rating);
            pic=(ImageView)itemView.findViewById(R.id.docter_image);
        }
    }
}
