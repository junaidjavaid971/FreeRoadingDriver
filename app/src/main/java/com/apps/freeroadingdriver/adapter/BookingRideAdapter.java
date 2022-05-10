package com.apps.freeroadingdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.eventbus.EventConstant;
import com.apps.freeroadingdriver.eventbus.EventObject;
import com.apps.freeroadingdriver.model.dataModel.CancelForIndividual;
import com.apps.freeroadingdriver.model.responseModel.Booking_data;
import com.apps.freeroadingdriver.network.URLConstant;
import com.apps.freeroadingdriver.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingRideAdapter extends RecyclerView.Adapter<BookingRideAdapter.Holder> {
    Context context;
    boolean isJourneyStarted;
    String rideStatus;
    BookingListener bookingListener;
    private ArrayList<Booking_data> paymentCardList;

    public BookingRideAdapter(Context context, ArrayList<Booking_data> paymentList, boolean isJourneyStarted, String rideStatus, BookingListener bookingListener) {
        this.context = context;
        paymentCardList = paymentList;
        this.isJourneyStarted = isJourneyStarted;
        this.rideStatus = rideStatus;
        this.bookingListener = bookingListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_active_ride_details, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final Booking_data data = paymentCardList.get(position);
        holder.passengerName.setText(data.getCustomer_name());
        String backToAppRideStatus = data.getStatus();
        GlideUtil.loadCircleImage(context, holder.loc1, URLConstant.Companion.getPASSENGER_IMAGE_URL() + data.getCustomer_profile_pic(), R.drawable.ic_profile_placeholder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookingListener.onCustomerclicked(position);
            }
        });
        holder.callPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventObject(EventConstant.CALL_PASSENGER, data.getCustomer_mobile()));
            }
        });

        holder.cancelForSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventObject(EventConstant.CANCEL_INDIVIDUAL_RIDE, new CancelForIndividual(data.getApp_appointment_id(), position, "", data.getDriver_type())));
            }
        });

        if (rideStatus.equals("3") || isJourneyStarted) {   //3 for ongoing ride and 1 for finish
            if (backToAppRideStatus.equals(AppConstant.ARRIVED_STATUS)) {
                holder.arrived.setVisibility(View.GONE);
                holder.pickup.setVisibility(View.VISIBLE);
                holder.drop.setVisibility(View.GONE);
                holder.pickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventObject(EventConstant.I_HAVE_ARRIVED, new CancelForIndividual(data.getApp_appointment_id(), position, AppConstant.PICKED_STATUS, data.getDriver_type())));
                    }
                });
            } else if (backToAppRideStatus.equals(AppConstant.PICKED_STATUS)) {
                holder.arrived.setVisibility(View.GONE);
                holder.pickup.setVisibility(View.GONE);
                holder.drop.setVisibility(View.VISIBLE);
                holder.cancelForSingle.setVisibility(View.INVISIBLE);
                holder.drop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventObject(EventConstant.I_HAVE_ARRIVED, new CancelForIndividual(data.getApp_appointment_id(), position, AppConstant.DROP_STATUS, data.getDriver_type())));
                    }
                });
            } else if (backToAppRideStatus.equals("6")) {
                holder.arrived.setVisibility(View.VISIBLE);
                holder.pickup.setVisibility(View.GONE);
                holder.drop.setVisibility(View.GONE);
                holder.arrived.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new EventObject(EventConstant.I_HAVE_ARRIVED, new CancelForIndividual(data.getApp_appointment_id(), position, AppConstant.ARRIVED_STATUS, data.getDriver_type())));
                    }
                });
            } else if (backToAppRideStatus.equals("2") || backToAppRideStatus.equals("1")) {
                holder.arrived.setVisibility(View.GONE);
                holder.pickup.setVisibility(View.GONE);
                holder.drop.setVisibility(View.GONE);
                holder.cancelForSingle.setVisibility(View.INVISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        EventBus.getDefault().post(new EventObject(EventConstant.I_HAVE_ARRIVED,new CancelForIndividual(data.getApp_appointment_id(),position,"1",data.getDriver_type())));
                    }
                });
            }
        } else {
            holder.arrived.setVisibility(View.GONE);
            holder.pickup.setVisibility(View.GONE);
            holder.drop.setVisibility(View.GONE);
            if (backToAppRideStatus.equals("6"))
                holder.cancelForSingle.setVisibility(View.VISIBLE);
            else
                holder.cancelForSingle.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return paymentCardList.size();
    }

    public interface BookingListener {
        void onCustomerclicked(int postition);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView passengerName;
        CircleImageView loc1;
        ImageView callPassenger, cancelForSingle;
        Button arrived, pickup, drop;

        public Holder(View itemView) {
            super(itemView);
            passengerName = itemView.findViewById(R.id.passengerName);
            loc1 = itemView.findViewById(R.id.loc1);
            callPassenger = itemView.findViewById(R.id.callPassenger);
            cancelForSingle = itemView.findViewById(R.id.cancelForSingle);
            arrived = itemView.findViewById(R.id.iHaveArrived);
            pickup = itemView.findViewById(R.id.pickUpPassenger);
            drop = itemView.findViewById(R.id.dropPassenger);
        }
    }
}
