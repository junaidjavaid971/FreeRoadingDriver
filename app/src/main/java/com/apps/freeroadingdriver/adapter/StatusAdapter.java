package com.apps.freeroadingdriver.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.freeroadingdriver.R;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusHolder> {
    Context context;
    ArrayList<String> list;
    StatusListener statusListener;

    public StatusAdapter(Context context, ArrayList<String> list, StatusListener statusListener) {
        this.context = context;
        this.list = list;
        this.statusListener = statusListener;
    }

    @NonNull
    @Override
    public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false);
        return new StatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHolder holder, int position) {
        holder.tv_status_title.setText(list.get(position));

        holder.itemView.setOnClickListener(view -> {
            if (list.get(position).equals(context.getString(R.string.status_stripe_connect))) {
                statusListener.onStripeClick();
            } else if (list.get(position).equals(context.getString(R.string.status_admin_aprovel))) {
                statusListener.onAdminClick();
            } else if (list.get(position).equals(context.getString(R.string.status_backend_approval))) {
                statusListener.backendClick();
            } else if (list.get(position).equals(context.getString(R.string.status_update_profile))) {
                statusListener.onProfileClick();
            } else if (list.get(position).equals(context.getString(R.string.status_update_vehical_details))) {
                statusListener.onVehicalClick();
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public interface StatusListener {
        void onStripeClick();

        void onAdminClick();

        void backendClick();

        void onVehicalClick();

        void onProfileClick();
    }

    class StatusHolder extends RecyclerView.ViewHolder {

        TextView tv_status_title;

        public StatusHolder(View itemView) {
            super(itemView);
            tv_status_title = itemView.findViewById(R.id.tv_status_title);
        }
    }
}
