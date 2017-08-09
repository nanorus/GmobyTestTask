package com.example.nanorus.gmobytesttask.di.routes_list;

import com.example.nanorus.gmobytesttask.model.ResourceManager;
import com.example.nanorus.gmobytesttask.presenter.routes_list.IRoutesListActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListActivityPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenter;
import com.example.nanorus.gmobytesttask.router.RoutesListRouter;

import dagger.Module;
import dagger.Provides;

@Module
public class RoutesListModule {

    @Provides
    IRoutesListActivityPresenter provideRoutesListActivityPresenter(ResourceManager resourceManager) {
        return new RoutesListActivityPresenter(resourceManager);
    }

    @Provides
    RoutesListFragmentPresenter provideRoutesListFragmentPresenter(ResourceManager resourceManager,
                                                                   RoutesListRouter routesListRouter) {
        return new RoutesListFragmentPresenter(resourceManager, routesListRouter);
    }

    @Provides
    RoutesListRouter provideRoutesListRouter(){
        return new RoutesListRouter();
    }


}
