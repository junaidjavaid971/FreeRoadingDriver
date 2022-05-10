package com.apps.freeroadingdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.model.responseModel.Road_trips;

import java.util.ArrayList;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.Holder> {
    Context context;
    private ArrayList<Road_trips> paymentCardList;
    OnClickRow onClickRow;
    public TripHistoryAdapter(Context context, ArrayList<Road_trips> paymentList, TripHistoryAdapter.OnClickRow onClickRow) {
        this.context = context;
        paymentCardList = paymentList;
        this.onClickRow = onClickRow;
    }

    @Override
    public TripHistoryAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ride_history,parent,false);
        return new TripHistoryAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(final TripHistoryAdapter.Holder holder, final int position) {
        if (paymentCardList!=null && paymentCardList.size()>0){
            final Road_trips item = paymentCardList.get(position);
            holder.bookingId.setText(item.getId());
            holder.pick_loc.setText(item.getPickup_location());
            holder.drop_loc.setText(item.getDroppoff_location());
            if (item.getDeparture_date().contains(" ")) {
                String date[] = item.getDeparture_date().split(" ");
                String part = date[0];
                holder.date.setText(part);
            }else {
                holder.date.setText(item.getDeparture_date());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRow.onRowClick(item);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return paymentCardList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView bookingId,pick_loc,drop_loc,date;
        public Holder(View itemView) {
            super(itemView);
            bookingId =  itemView.findViewById(R.id.bookingId);
            pick_loc =  itemView.findViewById(R.id.pick_loc);
            drop_loc =  itemView.findViewById(R.id.drop_loc);
            date =  itemView.findViewById(R.id.date);
        }
    }

    public interface OnClickRow{
        void onRowClick(Road_trips appointment_history);
    }
}
