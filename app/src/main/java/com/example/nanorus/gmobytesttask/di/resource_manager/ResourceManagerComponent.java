package com.example.nanorus.gmobytesttask.di.resource_manager;


import com.example.nanorus.gmobytesttask.model.ResourceManager;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Subcomponent(modules = {ResourceManagerModule.class})
@Singleton
public interface ResourceManagerComponent {

    ResourceManager resourceManager();

}
