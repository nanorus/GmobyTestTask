package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.presenter.base.PresenterFactory;

import javax.inject.Inject;

public class RoutesListFragmentPresenterFactory implements PresenterFactory<RoutesListFragmentPresenter> {

    @Inject
    RoutesListFragmentPresenter mRoutesListFragmentPresenter;


    public RoutesListFragmentPresenterFactory(){
        App.getApp().getRoutesListActivityComponent().inject(this);
    }

    @Override
    public RoutesListFragmentPresenter create() {
        return mRoutesListFragmentPresenter;
    }
}
