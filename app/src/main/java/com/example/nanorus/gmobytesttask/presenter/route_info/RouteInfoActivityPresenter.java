package com.example.nanorus.gmobytesttask.presenter.route_info;

import com.example.nanorus.gmobytesttask.view.route_info.IRouteInfoActivity;

public class RouteInfoActivityPresenter implements IRouteInfoActivityPresenter {

    private IRouteInfoActivity mView;

    public RouteInfoActivityPresenter() {
    }

    @Override
    public void bindView(IRouteInfoActivity activity) {
        mView = activity;
    }

    @Override
    public void releasePresenter() {
        mView = null;
    }
}
