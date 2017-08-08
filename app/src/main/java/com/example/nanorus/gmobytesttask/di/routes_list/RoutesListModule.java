package com.example.nanorus.gmobytesttask.di.routes_list;

import com.example.nanorus.gmobytesttask.model.ResourceManager;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class RoutesListModule {

    @Provides
    RoutesListActivityPresenter provideRoutesListActivityPresenter(ResourceManager resourceManager) {
        return new RoutesListActivityPresenter(resourceManager);
    }

    @Provides
    RoutesListFragmentPresenter provideRoutesListFragmentPresenter(ResourceManager resourceManager) {
        return new RoutesListFragmentPresenter(resourceManager);
    }
}
