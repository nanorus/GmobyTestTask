package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowAlertEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowAlertRetryOnlineLoading;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListUpdateOnlineEvent;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesActivity;
import com.squareup.otto.Subscribe;

public class RoutesActivityPresenter implements IRoutesActivityPresenter {

    IRoutesActivity mView;


    public RoutesActivityPresenter(IRoutesActivity view) {
        mView = view;
        EventBus.getInstance().register(this);
    }

    @Subscribe
    public void showAlert(RoutesListShowAlertEvent event) {
        if (event.isWillShown()) {
            mView.showAlert(event.getMessage());
        } else {
            mView.hideAlert();
        }
    }

    @Subscribe
    public void showRefreshingListener(RoutesListShowRefreshingEvent event) {
        if (event.isShowRefresh())
            mView.startShowRefreshing();
        else
            mView.stopShowRefreshing();
    }

    @Subscribe
    public void showAlertRetryOnlineLoadingListener(RoutesListShowAlertRetryOnlineLoading event) {
        mView.showAlertRetryOnlineLoading(event.getMessage());
    }

    @Override
    public void onRefresh() {

        if (InternetConnection.isOnline()) {
            EventBus.getInstance().post(new RoutesListUpdateOnlineEvent());
        } else {
            mView.stopShowRefreshing();
            mView.showAlertRetryOnlineLoading(App.getApp().getString(R.string.no_internet));
        }

    }

    @Override
    public void releasePresenter() {
        EventBus.getInstance().unregister(this);

        mView = null;
    }
}
