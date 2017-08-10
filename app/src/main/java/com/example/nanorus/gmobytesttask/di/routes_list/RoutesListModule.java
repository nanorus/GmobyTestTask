package com.example.nanorus.gmobytesttask.di.routes_list;

import com.example.nanorus.gmobytesttask.presenter.routes_list.IRoutesListActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListActivityPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RoutesListModule {

    @Binds
    abstract  IRoutesListActivityPresenter provideRoutesListActivityPresenter(RoutesListActivityPresenter routesListActivityPresenter);

}
