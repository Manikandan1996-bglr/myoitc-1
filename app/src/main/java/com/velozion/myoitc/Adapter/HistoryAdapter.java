package com.velozion.myoitc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.Activities.HistoryDetails;
import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.R;
import com.velozion.myoitc.db.HistoryData;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    Context context;
    ArrayList<HistoryData> data;
    int pos = 0;

    public HistoryAdapter(Context context, ArrayList<HistoryData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_history2, viewGroup, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {


        holder.checkinLoc.setText("" + data.get(position).getCheckinloc());
        holder.checkinTime.setText("" + data.get(position).getCheckintime());
        holder.checkoutLoc.setText("" + data.get(position).getCheckoutloc());
        holder.checkoutTime.setText("" + data.get(position).getCheckouttime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, HistoryDetails.class);
                intent.putExtra("data", data.get(position));
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

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView checkinLoc, checkoutLoc, checkinTime, checkoutTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            checkinLoc = itemView.findViewById(R.id.checkinloc);
            checkinTime = itemView.findViewById(R.id.checkintime);
            checkoutLoc = itemView.findViewById(R.id.checkoutloc);
            checkoutTime = itemView.findViewById(R.id.checkouttime);
        }
    }
}
