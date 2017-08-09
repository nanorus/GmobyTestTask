package com.example.nanorus.gmobytesttask.di.route_info;

import com.example.nanorus.gmobytesttask.view.route_info.RouteInfoActivity;
import com.example.nanorus.gmobytesttask.view.route_info.RouteInfoFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {RouteInfoModule.class})
public interface RouteInfoComponent {
    void inject(RouteInfoActivity activity);

    void inject(RouteInfoFragment fragment);
}
