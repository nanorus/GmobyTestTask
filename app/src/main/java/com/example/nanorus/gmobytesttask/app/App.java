package com.example.nanorus.gmobytesttask.app;


import android.app.Application;

public class App extends Application {

    public static App sInstance;

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
}
