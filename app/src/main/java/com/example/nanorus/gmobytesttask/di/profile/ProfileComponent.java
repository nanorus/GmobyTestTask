package com.example.nanorus.gmobytesttask.di.profile;

import com.example.nanorus.gmobytesttask.presenter.profile.ProfilePresenterFactory;

import dagger.Subcomponent;

@Subcomponent(modules = {ProfileModule.class})
public interface ProfileComponent {

    void inject(ProfilePresenterFactory profilePresenterFactory);

}
