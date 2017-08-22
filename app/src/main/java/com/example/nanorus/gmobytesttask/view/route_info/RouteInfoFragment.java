package com.example.nanorus.gmobytesttask.view.route_info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.App;
import com.example.nanorus.gmobytesttask.presenter.route_info.IRouteInfoFragmentPresenter;

import javax.inject.Inject;


public class RouteInfoFragment extends Fragment implements IRouteInfoFragment {

    @Inject
    IRouteInfoFragmentPresenter mPresenter;

    private TextView fragment_route_info_tv_fromCity;
    private TextView fragment_route_info_tv_toCity;
    private TextView fragment_route_info_tv_fromDate;
    private TextView fragment_route_info_tv_toDate;
    private TextView fragment_route_info_tv_price;
    private TextView fragment_route_info_tv_fromInfo;
    private TextView fragment_route_info_tv_toInfo;
    private TextView fragment_route_info_tv_info;
    private TextView fragment_route_info_tv_busId;
    private TextView fragment_route_info_tv_reservationCount;


    @Override
    public void onAttach(Activity activity) {
            super.onAttach(activity);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_info, container, false);

        fragment_route_info_tv_fromCity = (TextView) v.findViewById(R.id.fragment_route_info_tv_fromCity);
        fragment_route_info_tv_toCity = (TextView) v.findViewById(R.id.fragment_route_info_tv_toCity);
        fragment_route_info_tv_fromDate = (TextView) v.findViewById(R.id.fragment_route_info_tv_fromDate);
        fragment_route_info_tv_toDate = (TextView) v.findViewById(R.id.fragment_route_info_tv_toDate);
        fragment_route_info_tv_price = (TextView) v.findViewById(R.id.fragment_route_info_tv_price);
        fragment_route_info_tv_fromInfo = (TextView) v.findViewById(R.id.fragment_route_info_tv_fromInfo);
        fragment_route_info_tv_toInfo = (TextView) v.findViewById(R.id.fragment_route_info_tv_toInfo);
        fragment_route_info_tv_info = (TextView) v.findViewById(R.id.fragment_route_info_tv_info);
        fragment_route_info_tv_busId = (TextView) v.findViewById(R.id.fragment_route_info_tv_busId);
        fragment_route_info_tv_reservationCount = (TextView) v.findViewById(R.id.fragment_route_info_tv_reservationCount);

        App.getApp().getRouteInfoComponent().inject(this);
        mPresenter.bindView(this);
        mPresenter.loadAndShowData(this.getArguments().getInt("id"));

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        mPresenter.releasePresenter();
        mPresenter = null;
        super.onDetach();
    }

    @Override
    public void setFromCityField(String text) {
        if (text != null)
            fragment_route_info_tv_fromCity.setText(text);
    }

    @Override
    public void setToCityField(String text) {
        if (text != null)
            fragment_route_info_tv_toCity.setText(text);
    }

    @Override
    public void setFromDateField(String text) {
        if (text != null)
            fragment_route_info_tv_fromDate.setText(text);
    }


    @Override
    public void setToDateField(String text) {
        if (text != null)
            fragment_route_info_tv_toDate.setText(text);
    }


    @Override
    public void setToInfoField(String text) {
        if (text != null)
            fragment_route_info_tv_toInfo.setText(text);
    }

    @Override
    public void setFromInfoField(String text) {
        if (text != null)
            fragment_route_info_tv_fromInfo.setText(text);
    }

    @Override
    public void setPriceField(String text) {
        if (text != null)
            fragment_route_info_tv_price.setText(text);
    }

    @Override
    public void setInfoField(String text) {
        if (text != null)
            fragment_route_info_tv_info.setText(text);
    }

    @Override
    public void setBusIdField(String text) {
        if (text != null)
            fragment_route_info_tv_busId.setText(text);
    }

    @Override
    public void setReservationCountField(String text) {
        if (text != null)
            fragment_route_info_tv_reservationCount.setText(text);
    }

    @Override
    public IRouteInfoFragment getViewLayer() {
        return this;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
}
