package com.velozion.myoitc.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    Context context;
    ArrayList<String> data;

    String time_selected = null;
    int pos = -1;


    public TimeSlotAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_timeslot, viewGroup, false);
        TimeSlotViewHolder timeSlotViewHolder = new TimeSlotViewHolder(view);
        return timeSlotViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TimeSlotViewHolder holder, final int position) {


        holder.time.setText("" + data.get(position));

        if (pos == position) {
           // holder.time.setBackgroundResource(R.drawable.design_timeslot_checked);
            ManageSvg(holder.time,R.drawable.design_timeslot_checked);

            holder.time.setTextColor(context.getResources().getColor(R.color.white));
            holder.time.setPadding(12, 12, 12, 12);
            holder.time.setTextSize(16);
        } else {

            //holder.time.setBackgroundResource(R.drawable.design_timeslot);
            ManageSvg(holder.time,R.drawable.design_timeslot);
            holder.time.setTextColor(context.getResources().getColor(R.color.greyblack));
            holder.time.setPadding(12, 12, 12, 12);
            holder.time.setTextSize(16);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                time_selected = data.get(position);
                Log.d("Response", time_selected);
                pos = position;
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {

        TextView time;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.timeslot_time);

        }
    }

    public String getSelectedTimeSlot() {

        return time_selected;

    }


    void ManageSvg(TextView textView, int draw)
    {

        int data=getSelectedTheme();

        Resources.Theme theme = context.getTheme();
        theme.applyStyle(data, true);

        final Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),draw, theme);
        textView.setBackground(drawable);

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
