package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListActivity;

public class RoutesListActivityPresenter implements IRoutesListActivityPresenter {

    IRoutesListActivity mView;


    public RoutesListActivityPresenter(IRoutesListActivity view) {
        mView = view;
        EventBus.getInstance().register(this);
    }


    @Override
    public void onRefresh() {

        if (InternetConnection.isOnline()) {
            mView.updateRoutesListOnline();
           // EventBus.getInstance().post(new RoutesListUpdateOnlineEvent());
        } else {
            mView.showSwipeRefreshing(false);
            mView.showAlertRetryOnlineLoading(App.getApp().getString(R.string.no_internet));
        }

    }

    @Override
    public void releasePresenter() {
        EventBus.getInstance().unregister(this);

        mView = null;
    }
}
