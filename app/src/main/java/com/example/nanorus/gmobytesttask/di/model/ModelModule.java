package com.example.nanorus.gmobytesttask.di.model;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ModelModule {
    @Provides
    @Singleton
    @Named("routesRetroClient")
    Retrofit provideRoutesRetroClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://projects.gmoby.org/web/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

}
