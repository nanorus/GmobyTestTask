package com.example.nanorus.gmobytesttask.presenter.profile;

import com.example.nanorus.gmobytesttask.presenter.base.Presenter;
import com.example.nanorus.gmobytesttask.view.profile.IProfileActivity;

public class ProfilePresenter implements IProfilePresenter, Presenter<IProfileActivity> {

    private IProfileActivity mView;

    @Override
    public void onAvatarClicked() {
        // choose photo
        mView.takePhoto();
    }

    @Override
    public void onViewAttached(IProfileActivity view) {
        mView = view;
    }

    @Override
    public void onViewDetached() {
        mView = null;
    }

    @Override
    public void onDestroyed() {
        releasePresenter();
    }

    @Override
    public void releasePresenter() {
        mView = null;
    }
}
