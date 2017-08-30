package com.example.nanorus.gmobytesttask.di.model;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.database.DatabaseContract;
import com.example.nanorus.gmobytesttask.model.database.DatabaseHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @Provides
    @Singleton
    HttpURLConnection provideHttpURLConnection(String fromDate, String toDate){
        String url = "http://projects.gmoby.org/web/index.php/api/";

        URL obj = null;
        try {
            obj = new URL(url);
            return (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper (Context context, DatabaseContract databaseContract){
        return new DatabaseHelper(context, databaseContract);
    }

}
