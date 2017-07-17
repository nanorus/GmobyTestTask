package com.example.nanorus.gmobytesttask.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.presenter.RoutesPresenter;

import java.util.List;


public class RoutesListFragment extends Fragment implements IRoutesListFragment {

    RoutesPresenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routes_list, container, false);

        mPresenter = new RoutesPresenter(getViewLayer());

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void createAdapter() {

    }

    @Override
    public void setAdapter(List<RouteMainInfoPojo> routeMainInfoPojos) {

    }

    @Override
    public void updateAdapter(List<RouteMainInfoPojo> routeMainInfoPojos) {

    }

    @Override
    public IRoutesListFragment getViewLayer() {
        return this;
    }

    @Override
    public Context getViewContext() {
        return this.getActivity();
    }
}
