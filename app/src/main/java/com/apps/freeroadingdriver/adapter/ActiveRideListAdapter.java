package com.apps.freeroadingdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.interfaces.SearchRoadRideClick;
import com.apps.freeroadingdriver.model.responseModel.Road_trips;
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager;

import java.util.ArrayList;

public class ActiveRideListAdapter extends RecyclerView.Adapter<ActiveRideListAdapter.Holder> {
    Context context;
    private ArrayList<Road_trips> paymentCardList;
    private SearchRoadRideClick searchRoadRideClick;

    public ActiveRideListAdapter(Context context, ArrayList<Road_trips> paymentList, SearchRoadRideClick searchRoadRideClick) {
        this.context = context;
        paymentCardList = paymentList;
        this.searchRoadRideClick = searchRoadRideClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_active_ride, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final Road_trips data = paymentCardList.get(position);
        holder.bookingId.setText("" + data.getId());
        if (FreeRoadingPreferenceManager.getInstance().getRideRoadType()) {
            holder.labelBook.setText("Trip ID");
        } else {
            holder.labelBook.setText("Booking ID");
        }
        holder.pickup.setText(data.getPickup_location());
        holder.dropOff.setText(data.getDroppoff_location());
        holder.price.setText(" $" + data.getHow_much_ride_amount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRoadRideClick.takeDatatoFragment(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentCardList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView pickup, dropOff, price, numberOfActiveUsers, bookingId, labelBook;

        public Holder(View itemView) {
            super(itemView);
            bookingId = itemView.findViewById(R.id.bookingId);
            pickup = itemView.findViewById(R.id.pick_loc);
            dropOff = itemView.findViewById(R.id.drop_loc);
            price = itemView.findViewById(R.id.price);
            labelBook = itemView.findViewById(R.id.labelBook);
            numberOfActiveUsers = itemView.findViewById(R.id.numberofActivities);
        }
    }
}
