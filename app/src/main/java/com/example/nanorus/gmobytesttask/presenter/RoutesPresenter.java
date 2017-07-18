package com.example.nanorus.gmobytesttask.presenter;

import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.view.IRoutesListFragment;

import java.util.List;

import rx.Observable;
import rx.Subscription;

public class RoutesPresenter implements IRoutesPresenter {

    IRoutesListFragment mView;

    Subscription updateListOnlineSubscription;

    public RoutesPresenter(IRoutesListFragment view) {
        mView = view;
        mView.createAndSetAdapter();
        updateListOnline();
    }

    @Override
    public void updateListOnline() {
        Observable<List<RouteMainInfoPojo>> routesMainInfos = DataManager.getRoutesMainInfoListOnline(20140101, 20170501);
        updateListOnlineSubscription = routesMainInfos.subscribe(
                routeMainInfoPojos -> mView.updateAdapter(routeMainInfoPojos),
                throwable -> System.out.println(throwable.getMessage()),
                () -> updateListOnlineSubscription.unsubscribe()
        );
    }

    @Override
    public void updateListOffline() {

    }

    @Override
    public void releasePresenter() {
        mView = null;
        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed())
            updateListOnlineSubscription.unsubscribe();
    }
}
