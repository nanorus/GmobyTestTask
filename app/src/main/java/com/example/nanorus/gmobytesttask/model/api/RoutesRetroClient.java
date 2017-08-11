package com.example.nanorus.gmobytesttask.model.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoutesRetroClient {

    private static Retrofit sInstance = null;

    public static Retrofit getInstance() {
        if (sInstance == null) {
            sInstance = new Retrofit.Builder()
                    .baseUrl("http://projects.gmoby.org/web/index.php/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return sInstance;
    }

    @Override
    protected void finalize() throws Throwable {
        sInstance = null;
        super.finalize();
    }
}
