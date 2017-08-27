package com.example.nanorus.gmobytesttask.di.profile;

import com.example.nanorus.gmobytesttask.presenter.profile.IProfilePresenter;
import com.example.nanorus.gmobytesttask.presenter.profile.ProfilePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileModule {

    @Provides
    IProfilePresenter provideProfilePresenter(){
        return new ProfilePresenter();
    }

}
