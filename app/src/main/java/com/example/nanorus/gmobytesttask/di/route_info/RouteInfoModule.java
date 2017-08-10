package com.example.nanorus.gmobytesttask.di.route_info;

import com.example.nanorus.gmobytesttask.presenter.route_info.IRouteInfoActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.route_info.IRouteInfoFragmentPresenter;
import com.example.nanorus.gmobytesttask.presenter.route_info.RouteInfoActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.route_info.RouteInfoFragmentPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RouteInfoModule {

    @Binds
    abstract IRouteInfoFragmentPresenter provideRouteInfoFragmentPresenter(RouteInfoFragmentPresenter routeInfoFragmentPresenter);

    @Binds
    abstract IRouteInfoActivityPresenter provideRouteInfoActivityPresenter(RouteInfoActivityPresenter routeInfoActivityPresenter);
}
