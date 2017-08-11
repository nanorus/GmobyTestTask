package com.example.nanorus.gmobytesttask.app;


import android.app.Application;

import com.example.nanorus.gmobytesttask.di.app.AppComponent;
import com.example.nanorus.gmobytesttask.di.app.AppModule;
import com.example.nanorus.gmobytesttask.di.app.DaggerAppComponent;
import com.example.nanorus.gmobytesttask.di.route_info.RouteInfoComponent;
import com.example.nanorus.gmobytesttask.di.routes_list.RoutesListComponent;

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
        clearAppComponent();
        super.finalize();
    }

    @Override
    public void onTerminate() {
        sInstance = null;
        clearAppComponent();
        super.onTerminate();
    }

    private RouteInfoComponent mRouteInfoComponent;
    private RoutesListComponent mRoutesListComponent;

    private AppComponent mAppComponent;

    public RouteInfoComponent getRouteInfoComponent() {
        if (mRouteInfoComponent == null)
            mRouteInfoComponent = getAppComponent().plusRouteInfoComponent();
        return mRouteInfoComponent;
    }

    public void clearRouteInfoComponent() {
        mRouteInfoComponent = null;
    }

    public RoutesListComponent getRoutesListComponent() {
        if (mRoutesListComponent == null)
            mRoutesListComponent = getAppComponent().plusRoutesListComponent();
        return mRoutesListComponent;
    }

    public void clearRoutesListComponent() {
        mRoutesListComponent = null;
    }

    public AppComponent getAppComponent() {
        if (mAppComponent == null)
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(getApp().getApplicationContext()))
                    .build();
        return mAppComponent;
    }

    public void clearAppComponent() {
        mAppComponent = null;
    }

}
