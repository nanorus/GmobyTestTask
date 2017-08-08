package com.example.nanorus.gmobytesttask.di.resource_manager;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.ResourceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ResourceManagerModule {

    @Provides
    @Singleton
    ResourceManager provideResourceManager(Context context){
        return new ResourceManager(context);
    }

}
