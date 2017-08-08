package com.example.nanorus.gmobytesttask.app;


import android.app.Application;

import com.example.nanorus.gmobytesttask.di.app.AppComponent;
import com.example.nanorus.gmobytesttask.di.app.AppModule;
import com.example.nanorus.gmobytesttask.di.app.DaggerAppComponent;
import com.example.nanorus.gmobytesttask.di.resource_manager.ResourceManagerComponent;
import com.example.nanorus.gmobytesttask.di.resource_manager.ResourceManagerModule;
import com.example.nanorus.gmobytesttask.di.routes_list.RoutesListComponent;
import com.example.nanorus.gmobytesttask.di.routes_list.RoutesListModule;

public class App extends Application {

    private static App sInstance;

    public App() {
        sInstance = this;
    }

    public static App getApp() {
        return sInstance;
    }

    @Override
    protected void finalize() throws Throwable {
        sInstance = null;
        super.finalize();
    }


    private RoutesListComponent mRoutesListActivityComponent;

    private AppComponent mAppComponent;
    private ResourceManagerComponent mResourceManagerComponent;


    public AppComponent getAppComponent() {
        if (mAppComponent == null)
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(getApp().getApplicationContext()))
                    .build();
        return mAppComponent;
    }

    public ResourceManagerComponent getResourceManagerComponent() {
        if (mResourceManagerComponent == null)
            mResourceManagerComponent = getAppComponent().plusResourceManagerComponent(new ResourceManagerModule());
        return mResourceManagerComponent;
    }

    public RoutesListComponent getRoutesListActivityComponent() {
        if (mRoutesListActivityComponent == null)
            mRoutesListActivityComponent = getAppComponent().plusRoutesListActivityComponent(new RoutesListModule());
        return mRoutesListActivityComponent;
    }
}
