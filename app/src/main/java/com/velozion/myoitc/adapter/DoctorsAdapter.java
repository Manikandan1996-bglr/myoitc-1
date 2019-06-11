package com.velozion.myoitc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.R;
import com.velozion.myoitc.activities.BookAppointment;
import com.velozion.myoitc.activities.DisplayFullImage;
import com.velozion.myoitc.activities.DocterDetails;
import com.velozion.myoitc.databinding.ItemDoctersBinding;
import com.velozion.myoitc.db.DoctorProfileData;

import java.util.ArrayList;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder> {

    Context context;
    ArrayList<DoctorProfileData> data;
    int pos = 0;

    LayoutInflater layoutInflater;


    public DoctorsAdapter(Context context, ArrayList<DoctorProfileData> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        ItemDoctersBinding itemDoctersBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_docters, viewGroup, false);

        return new DoctorsViewHolder(itemDoctersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, final int position) {


        holder.itemDoctersBinding.setDoctorProfile(data.get(position));

        holder.itemDoctersBinding.docterProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocterDetails.class);
                intent.putExtra("data", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.itemDoctersBinding.docterBookappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BookAppointment.class);
                intent.putExtra("data", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        holder.itemDoctersBinding.docterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Utils.displayFullImage(context,data.get(position).getPic());

                Intent intent = new Intent(context, DisplayFullImage.class);
                intent.putExtra("profile_name", data.get(position).getName());
                intent.putExtra("profile_pic", data.get(position).getPic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

      /*  holder.name.setText("" + data.get(position).getName());
        holder.education.setText("" + data.get(position).getQualification());
        holder.specalist.setText("" + data.get(position).getSpecialist() + "  \u2022 " + data.get(position).getExperience());
        holder.ratingBar.setRating(Float.parseFloat(data.get(position).getRating()));

        holder.locality.setText("" + data.get(position).getHospital_name() + " , " + data.get(position).getArea());
        holder.fees.setText(context.getResources().getString(R.string.us) + " " + data.get(position).getFees());
        holder.mobile.setText("" + data.get(position).getMobile());

        ImageLoader.getInstance().displayImage(data.get(position).getPic(), holder.pic, options);


        holder.view_prfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DocterDetails.class);
                intent.putExtra("data", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BookAppointment.class);
                intent.putExtra("data", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.displayFullImage(context,data.get(position).getPic());

            }
        });*/


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

    class DoctorsViewHolder extends RecyclerView.ViewHolder {


        ItemDoctersBinding itemDoctersBinding;

        public DoctorsViewHolder(ItemDoctersBinding doctersBinding) {
            super(doctersBinding.getRoot());
            itemDoctersBinding = doctersBinding;


        }
    }


}
