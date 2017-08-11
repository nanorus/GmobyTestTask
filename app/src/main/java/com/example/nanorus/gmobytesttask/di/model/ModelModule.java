package com.example.nanorus.gmobytesttask.di.model;

import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;

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
    Retrofit provideRoutesRetroClient() {
        return new Retrofit.Builder()
                .baseUrl("http://projects.gmoby.org/web/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    GetAllRoutersService provideGetAllRoutesService(Retrofit routesRetroClient){
        return routesRetroClient.create(GetAllRoutersService.class);
    }

}
