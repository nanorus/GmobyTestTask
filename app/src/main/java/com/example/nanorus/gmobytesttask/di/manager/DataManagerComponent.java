package com.example.nanorus.gmobytesttask.di.manager;

import com.example.nanorus.gmobytesttask.image.ImageManager;
import com.example.nanorus.gmobytesttask.model.services.DownloadRoutesListService;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Subcomponent(modules = {DataManagerModule.class})
@Singleton
public interface DataManagerComponent {

    void inject(DownloadRoutesListService downloadRoutesListService);

    void inject(ImageManager imageManager);

}
