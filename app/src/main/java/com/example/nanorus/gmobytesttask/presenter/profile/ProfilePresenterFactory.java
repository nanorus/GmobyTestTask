package com.example.nanorus.gmobytesttask.presenter.profile;

import com.example.nanorus.gmobytesttask.App;
import com.example.nanorus.gmobytesttask.presenter.base.PresenterFactory;

import javax.inject.Inject;

public class ProfilePresenterFactory implements PresenterFactory<ProfilePresenter> {

    @Inject
    IProfilePresenter mPresenter;

    public ProfilePresenterFactory() {
        App.getApp().getAppComponent().plusProfileComponent().inject(this);
    }

    @Override
    public ProfilePresenter create() {
        return (ProfilePresenter) mPresenter;
    }


}
