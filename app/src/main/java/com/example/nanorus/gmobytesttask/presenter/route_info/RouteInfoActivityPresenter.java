package com.example.nanorus.gmobytesttask.presenter.route_info;

import com.example.nanorus.gmobytesttask.view.route_info.IRouteInfoActivity;

public class RouteInfoActivityPresenter implements IRouteInfoActivityPresenter {

    private IRouteInfoActivity mView;

    public RouteInfoActivityPresenter(IRouteInfoActivity view) {
        mView = view;
    }

    @Override
    public void releasePresenter() {
        mView = null;
    }
}
