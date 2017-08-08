package com.example.nanorus.gmobytesttask.di.routes_list;

import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenterFactory;
import com.example.nanorus.gmobytesttask.view.routes_list.RoutesListActivity;
import com.example.nanorus.gmobytesttask.view.routes_list.RoutesListFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {RoutesListModule.class})
public interface RoutesListComponent {

    void inject(RoutesListActivity activity);

    void inject(RoutesListFragment fragment);

    void inject(RoutesListFragmentPresenterFactory factory);

}
