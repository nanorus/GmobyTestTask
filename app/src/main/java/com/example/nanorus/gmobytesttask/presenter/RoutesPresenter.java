package com.example.nanorus.gmobytesttask.presenter;

import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.ShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.UpdateRoutesListEvent;
import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.view.IRoutesListFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoutesPresenter implements IRoutesPresenter {

    private IRoutesListFragment mView;

    private Subscription updateListOnlineSubscription;
    private Subscription updateListOfflineSubscription;

    public RoutesPresenter(IRoutesListFragment view) {
        mView = view;
        mView.createAndSetAdapter();
        //   updateListOnline();
        updateListOffline();

        EventBus.getInstance().register(this);
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
                    EventBus.getInstance().post(new ShowRefreshingEvent(false));
                    updateListOnlineSubscription.unsubscribe();
                }
        );
    }

    @Override
    public void updateListOffline() {
        Observable<RouteMainInfoPojo> routeMainInfoPojoObservable = DataManager.loadRoutesMainInfoOffline(20140101, 20170501);
        updateListOfflineSubscription = routeMainInfoPojoObservable.subscribe(
                routeMainInfoPojo -> mView.addDataToListAndUpdateAdapter(routeMainInfoPojo),
                throwable -> System.out.println(throwable.getMessage()),
                () -> {
                    updateListOfflineSubscription.unsubscribe();
                }
        );
    }

    @Override
    public void releasePresenter() {
        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed())
            updateListOnlineSubscription.unsubscribe();
        if (updateListOfflineSubscription != null && !updateListOfflineSubscription.isUnsubscribed())
            updateListOfflineSubscription.unsubscribe();

        mView = null;
    }

    @Subscribe
    public void updateListOnlineListener(UpdateRoutesListEvent event) {
        updateListOnline();
    }
}
