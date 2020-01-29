package com.velozion.myoitc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.AnimUtils;
import com.velozion.myoitc.R;
import com.velozion.myoitc.activities.HistoryDetails;
import com.velozion.myoitc.bean.CheckInHistoryBean;
import com.velozion.myoitc.databinding.ItemHistory2Binding;

import java.util.ArrayList;

public class HistoryBindingAdapter extends RecyclerView.Adapter<HistoryBindingAdapter.HistoryHolder> implements Filterable {

    private Context context;
    private ArrayList<CheckInHistoryBean> data;
    private LayoutInflater layoutInflater;
    private int pos = 0;
    ArrayList<CheckInHistoryBean> temp;


    public HistoryBindingAdapter(Context context, ArrayList<CheckInHistoryBean> data) {
        this.context = context;
        this.data = data;
        this.temp = new ArrayList<>( data );

    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from( viewGroup.getContext() );
        }

        ItemHistory2Binding binding = DataBindingUtil.inflate( layoutInflater, R.layout.item_history2, viewGroup, false );

        return new HistoryHolder( binding );
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder historyHolder, final int position) {

        historyHolder.itemHistory2Binding.setHistory( data.get( position ) );


        historyHolder.itemView.getRootView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, HistoryDetails.class );
                intent.putExtra( "data", data.get( position ) );
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity( intent );
            }
        } );


        if (position > pos) {
            AnimUtils.animate( historyHolder.itemView.getRootView(), true );
        } else {
            AnimUtils.animate( historyHolder.itemView.getRootView(), false );
        }
        pos = position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {

        return exFilter;
    }

    public Filter exFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CheckInHistoryBean> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll( temp );
            } else {
                String strdata = charSequence.toString().toLowerCase().trim();
                for (CheckInHistoryBean checkInHistoryItem : temp) {
                    if (checkInHistoryItem.getClientName().toLowerCase().contains( strdata )) {
                        filteredList.add( checkInHistoryItem );
                    } else if (filteredList == null) {
                        Toast.makeText( context, "No Data", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (data != null) {
                data.clear();
                data.addAll( (ArrayList) filterResults.values );
                notifyDataSetChanged();
            }
        }
    };

    class HistoryHolder extends RecyclerView.ViewHolder {
        ItemHistory2Binding itemHistory2Binding;

        public HistoryHolder(ItemHistory2Binding itemView) {
            super( itemView.getRoot() );
            itemHistory2Binding = itemView;
        }
    }
}
