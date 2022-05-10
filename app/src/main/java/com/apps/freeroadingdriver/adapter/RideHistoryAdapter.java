package com.apps.freeroadingdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.model.responseModel.Home_data;

import java.util.ArrayList;

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.Holder> {
    Context context;
    private ArrayList<Home_data> paymentCardList;
    OnClickRow onClickRow;
    public RideHistoryAdapter(Context context, ArrayList<Home_data> paymentList, OnClickRow onClickRow) {
        this.context = context;
        paymentCardList = paymentList;
        this.onClickRow = onClickRow;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ride_history,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (paymentCardList!=null && paymentCardList.size()>0){
            final Home_data item = paymentCardList.get(position);
            holder.bookingId.setText(item.getApp_appointment_id());
            holder.pick_loc.setText(item.getPick_address());
            holder.drop_loc.setText(item.getDrop_address());
            if (item.getAppointment_date().contains(" ")) {
                String date[] = item.getAppointment_date().split(" ");
                String part = date[0];
                holder.date.setText(part);
            }else {
                holder.date.setText(item.getAppointment_date());
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
        void onRowClick(Home_data appointment_history);
    }
}
