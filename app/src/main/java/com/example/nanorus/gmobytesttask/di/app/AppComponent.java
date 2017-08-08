package com.example.nanorus.gmobytesttask.di.app;

import com.example.nanorus.gmobytesttask.di.resource_manager.ResourceManagerComponent;
import com.example.nanorus.gmobytesttask.di.resource_manager.ResourceManagerModule;
import com.example.nanorus.gmobytesttask.di.routes_list.RoutesListComponent;
import com.example.nanorus.gmobytesttask.di.routes_list.RoutesListModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    ResourceManagerComponent plusResourceManagerComponent(ResourceManagerModule resourceManagerModule);

    RoutesListComponent plusRoutesListActivityComponent(RoutesListModule routesListActivityModule);
}

