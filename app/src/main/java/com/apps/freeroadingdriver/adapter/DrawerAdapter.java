package com.apps.freeroadingdriver.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.freeroadingdriver.BuildConfig;
import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.model.dataModel.DrawerItem;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private static String TAG = DrawerAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<DrawerItem> drawerItems;
    private MyClickListener listener;

    public interface MyClickListener {
        void onDrawerItemClick(DrawerItem item);
    }

    public DrawerAdapter(Context context, ArrayList<DrawerItem> drawerItems, MyClickListener listener) {
        this.context = context;
        this.drawerItems = drawerItems;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_drawer_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DrawerItem drawerItem = drawerItems.get(position);
        if (drawerItem.getName().equals(context.getString(R.string.menu_logout))) {
            holder.tv_version.setVisibility(View.VISIBLE);
            holder.tv_version.setText(String.format(context.getString(R.string.app_version), BuildConfig.VERSION_NAME));
        } else {
            holder.tv_version.setVisibility(View.GONE);
        }
        holder.rlParentView.setSelected(drawerItem.isSelected());
        holder.tvDrawerItem.setText(drawerItem.getName());
        holder.tvDrawerItem2.setText(drawerItem.getName2());

        holder.ivDrawerItem.setImageDrawable(context.getResources().getDrawable(drawerItem.getIcon()));

    }

    @Override
    public int getItemCount() {
        if (drawerItems == null) {
            return 0;
        }
        return drawerItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlParentView;
        ImageView ivDrawerItem;
        TextView tvDrawerItem, tvDrawerItem2;
        TextView tv_version;
        ViewHolder(@Nullable View view) {
            super(view);
            rlParentView = view.findViewById(R.id.rl_parent_view);
            ivDrawerItem = view.findViewById(R.id.iv_drawer_item);
            tvDrawerItem = view.findViewById(R.id.tv_drawer_item);
            tvDrawerItem2 = view.findViewById(R.id.tv_drawer_item2);
            tv_version = view.findViewById(R.id.tv_version);
            rlParentView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rl_parent_view) {
                listener.onDrawerItemClick(drawerItems.get(getAdapterPosition()));
            }
        }
    }
}
