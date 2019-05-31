package com.velozion.myoitc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.velozion.myoitc.Activities.HistoryDetails;
import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeSlotAdapter extends RecyclerView.Adapter <TimeSlotAdapter.TimeSlotViewHolder>{

    Context context;
    ArrayList<String> data;

    String time_selected=null;
    int pos=-1;


    public TimeSlotAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_timeslot,viewGroup,false);
        TimeSlotViewHolder timeSlotViewHolder=new TimeSlotViewHolder(view);
        return timeSlotViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TimeSlotViewHolder holder, final int position) {


        holder.time.setText(""+data.get(position));

        if (pos==position)
        {
            holder.time.setBackgroundResource(R.drawable.design_timeslot_checked);
            holder.time.setTextColor(context.getResources().getColor(R.color.white));
            holder.time.setPadding(12,12,12,12);
            holder.time.setTextSize(16);
        }else {

            holder.time.setBackgroundResource(R.drawable.design_timeslot);
            holder.time.setTextColor(context.getResources().getColor(R.color.greyblack));
            holder.time.setPadding(12,12,12,12);
            holder.time.setTextSize(16);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               time_selected=data.get(position);
               Log.d("Response",time_selected);
               pos=position;
               notifyDataSetChanged();

            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            time=(TextView)itemView.findViewById(R.id.timeslot_time);

        }
    }

    public String getSelectedTimeSlot()
    {

       return time_selected;

    }
}
