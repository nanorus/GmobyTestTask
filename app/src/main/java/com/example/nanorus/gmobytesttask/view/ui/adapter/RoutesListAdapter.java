package com.example.nanorus.gmobytesttask.view.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;

import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.RoutesListViewHolder> {

    List<RouteMainInfoPojo> mData;

    public RoutesListAdapter(List<RouteMainInfoPojo> data) {
        mData = data;
    }

    @Override
    public RoutesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_list_item, parent, false);
        RoutesListViewHolder vh = new RoutesListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RoutesListViewHolder holder, int position) {
        holder.routes_list_item_tv_fromCity.setText(mData.get(position).getFromCityName());
        holder.routes_list_item_tv_toCity.setText(mData.get(position).getToCityName());
        holder.routes_list_item_tv_fromDate.setText(mData.get(position).getFromDate());
        holder.routes_list_item_tv_toDate.setText(mData.get(position).getToDate());
        holder.routes_list_item_tv_price.setText(String.valueOf(mData.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        else
            return 0;
    }


    public static class RoutesListViewHolder extends RecyclerView.ViewHolder {

        TextView routes_list_item_tv_fromCity;
        TextView routes_list_item_tv_toCity;
        TextView routes_list_item_tv_fromDate;
        TextView routes_list_item_tv_toDate;
        TextView routes_list_item_tv_price;

        public RoutesListViewHolder(View itemView) {
            super(itemView);
            routes_list_item_tv_fromCity = (TextView) itemView.findViewById(R.id.routes_list_item_tv_fromCity);
            routes_list_item_tv_toCity = (TextView) itemView.findViewById(R.id.routes_list_item_tv_toCity);
            routes_list_item_tv_fromDate = (TextView) itemView.findViewById(R.id.routes_list_item_tv_fromDate);
            routes_list_item_tv_toDate = (TextView) itemView.findViewById(R.id.routes_list_item_tv_toDate);
            routes_list_item_tv_price = (TextView) itemView.findViewById(R.id.routes_list_item_tv_price);
        }
    }

}
