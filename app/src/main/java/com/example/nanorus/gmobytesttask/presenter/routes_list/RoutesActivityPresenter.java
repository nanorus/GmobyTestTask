package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.RoutesListShowAlertEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.ShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.UpdateRoutesListOnlineEvent;
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
    public void showRefreshingListener(ShowRefreshingEvent event) {
        if (event.isShowRefresh())
            mView.startShowRefreshing();
        else
            mView.stopShowRefreshing();
    }


    @Override
    public void onRefresh() {

        if (InternetConnection.isOnline()) {
            EventBus.getInstance().post(new UpdateRoutesListOnlineEvent());
        } else {
            mView.stopShowRefreshing();
            mView.showAlertNoInternet();
        }

    }

    @Override
    public void releasePresenter() {
        EventBus.getInstance().unregister(this);

        mView = null;
    }
}
