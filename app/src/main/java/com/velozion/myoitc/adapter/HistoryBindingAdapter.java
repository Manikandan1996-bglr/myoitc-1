package com.velozion.myoitc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.activities.HistoryDetails;
import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.R;
import com.velozion.myoitc.databinding.ItemHistory2Binding;
import com.velozion.myoitc.db.HistoryData;

import java.util.ArrayList;

public class HistoryBindingAdapter extends RecyclerView.Adapter<HistoryBindingAdapter.HistoryHolder> {

    Context context;
    ArrayList<HistoryData> data;
    LayoutInflater layoutInflater;
    int pos = 0;

    public HistoryBindingAdapter(Context context, ArrayList<HistoryData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        ItemHistory2Binding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_history2, viewGroup, false);

        return new HistoryHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder historyHolder, final int position) {

        historyHolder.itemHistory2Binding.setHistory(data.get(position));


        historyHolder.itemView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoryDetails.class);
                intent.putExtra("data", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


        if (position > pos) {

            AnimUtils.animate(historyHolder.itemView.getRootView(), true);
        } else {
            AnimUtils.animate(historyHolder.itemView.getRootView(), false);

        }
        pos = position;


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        ItemHistory2Binding itemHistory2Binding;

        public HistoryHolder(ItemHistory2Binding itemView) {
            super(itemView.getRoot());
            itemHistory2Binding = itemView;
        }
    }
}
