package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.ResourceManager;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListActivity;

import javax.inject.Inject;

public class RoutesListActivityPresenter implements IRoutesListActivityPresenter {

    private IRoutesListActivity mView;

    private ResourceManager mResourceManager;
    private InternetConnection mInternetConnection;

    @Inject
    public RoutesListActivityPresenter(ResourceManager resourceManager,
                                       InternetConnection internetConnection) {
        mResourceManager = resourceManager;
        mInternetConnection = internetConnection;
    }

    @Override
    public void onRefresh() {
        if (mInternetConnection.isOnline()) {
            mView.updateRoutesListOnline();
        } else {
            mView.showSwipeRefreshing(false);
            mView.showAlertRetryOnlineLoading(mResourceManager.getString(R.string.no_internet));
        }
    }

    @Override
    public void releasePresenter() {
        mView = null;
    }

    @Override
    public void bindView(IRoutesListActivity activity) {
        mView = activity;
    }
}
