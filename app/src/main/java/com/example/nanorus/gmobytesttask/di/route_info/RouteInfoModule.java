package com.example.nanorus.gmobytesttask.di.route_info;

import com.example.nanorus.gmobytesttask.presenter.route_info.IRouteInfoActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.route_info.IRouteInfoFragmentPresenter;
import com.example.nanorus.gmobytesttask.presenter.route_info.RouteInfoActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.route_info.RouteInfoFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteInfoModule {
    @Provides
    IRouteInfoActivityPresenter provideRouteInfoActivityPresenter() {
        return new RouteInfoActivityPresenter();
    }

    @Provides
    IRouteInfoFragmentPresenter provideRouteInfoFragmentPresenter(){
        return new RouteInfoFragmentPresenter();
    }
}
