package com.example.nanorus.gmobytesttask.di.app;

import com.example.nanorus.gmobytesttask.di.model.ModelModule;
import com.example.nanorus.gmobytesttask.di.route_info.RouteInfoComponent;
import com.example.nanorus.gmobytesttask.di.routes_list.RoutesListComponent;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, ModelModule.class})
@Singleton
public interface AppComponent {

    RoutesListComponent plusRoutesListComponent();

    RouteInfoComponent plusRouteInfoComponent();

}

