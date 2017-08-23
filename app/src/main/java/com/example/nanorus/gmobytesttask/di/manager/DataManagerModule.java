package com.example.nanorus.gmobytesttask.di.manager;

import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.DataMapper;
import com.example.nanorus.gmobytesttask.model.ServiceManager;
import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.database.DatabaseManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataManagerModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    DataMapper provideDataMapper(Gson gson) {
        return new DataMapper(gson);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(DatabaseManager databaseManager, GetAllRoutersService getAllRoutersService,
                                   ServiceManager serviceManager) {
        return new DataManager(databaseManager, getAllRoutersService, serviceManager);
    }

}
