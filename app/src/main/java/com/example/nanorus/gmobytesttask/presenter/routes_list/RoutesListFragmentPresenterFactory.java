package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.presenter_base.PresenterFactory;

public class RoutesListFragmentPresenterFactory implements PresenterFactory<RoutesListFragmentPresenter> {



    @Override
    public RoutesListFragmentPresenter create() {
        return new RoutesListFragmentPresenter();
    }
}
