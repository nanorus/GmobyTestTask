package com.example.nanorus.gmobytesttask.presenter;

import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.view.IRoutesListFragment;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();
        final RequestPojo[] request = new RequestPojo[1];

        Observable<RequestPojo> requestPojoObservable = DataManager.loadRoutesOnline(20140101, 20170501)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        updateListOnlineSubscription = requestPojoObservable.subscribe(
                requestPojo -> {
                    routeMainInfoPojos.addAll(DataConverter.convertFullRoutesListToMainInfoRouteList(requestPojo.getData()));
                    mView.updateAdapter(routeMainInfoPojos);
                    request[0] = requestPojo;
                },

                throwable -> System.out.println(throwable.getMessage()),

                () -> {
                    DataManager.saveRoutes(request[0]);
                    updateListOnlineSubscription.unsubscribe();
                }
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
