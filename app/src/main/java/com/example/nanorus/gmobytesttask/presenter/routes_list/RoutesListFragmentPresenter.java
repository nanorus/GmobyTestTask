package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.app.Router;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListSetIsCachingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListSetIsOnlineLoadingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowAlertEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowAlertRetryOnlineLoading;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowNoDataEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListUpdateOfflineEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListUpdateOnlineEvent;
import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import rx.Completable;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoutesListFragmentPresenter implements IRoutesListFragmentPresenter {

    private IRoutesListFragment mView;

    private Subscription updateListOnlineSubscription;
    private Subscription updateListOfflineSubscription;
    Observable<RouteMainInfoPojo> routeMainInfoPojoObservable;
    Observable<RequestPojo> requestPojoObservable;

    private int mFromDate = 20140101;
    private int mToDate = 20180501;

    RequestPojo[] request;

    public RoutesListFragmentPresenter(IRoutesListFragment view) {

        mView = view;
        mView.createAndSetAdapter();
        EventBus.getInstance().register(this);

        if (mView.isOnlineLoading()) {
            mView.showAlertLoading();
        } else if (mView.isCaching()) {
            mView.showAlertInsert();
        } else {
            updateListOffline();
        }


    }


    @Override
    public void updateListOnline() {

        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed()) {
            updateListOnlineSubscription.unsubscribe();
        }

        EventBus.getInstance().post(new RoutesListSetIsOnlineLoadingEvent(true));

        mView.showAlertLoading();
        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();
        request = new RequestPojo[1];

        requestPojoObservable = DataManager.loadRoutesOnline(mFromDate, mToDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        updateListOnlineSubscription = requestPojoObservable.subscribe(
                requestPojo -> {
                    try {
                        routeMainInfoPojos.addAll(DataConverter.convertFullRoutesListToMainInfoRouteList(requestPojo.getData()));
                        request[0] = requestPojo;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                },

                throwable -> {
                    throwable.printStackTrace();
                    EventBus.getInstance().post(new RoutesListShowAlertEvent(null, false));
                    EventBus.getInstance().post(new RoutesListSetIsOnlineLoadingEvent(false));
                    EventBus.getInstance().post(new RoutesListShowAlertRetryOnlineLoading("Загрузка не успешна"));
                },

                () -> {
                    EventBus.getInstance().post(new RoutesListSetIsOnlineLoadingEvent(false));
                    if (request[0].isSuccess()) {
                        if (request[0].getData().size() == 0)
                            EventBus.getInstance().post(new RoutesListShowNoDataEvent(true));
                        else
                            EventBus.getInstance().post(new RoutesListShowNoDataEvent(false));
                        saveToDatabase();
                    } else {
                        EventBus.getInstance().post(new RoutesListShowAlertEvent(App.getApp().getString(R.string.server_error), true));
                    }
                }
        );
    }

    @Override
    public void updateListOffline() {
        if (mView.isCaching()) {
        } else {
            EventBus.getInstance().post(new RoutesListShowAlertEvent("Открытие", true));
            routeMainInfoPojoObservable = DataManager.loadRoutesMainInfoOffline(mFromDate, mToDate)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            ;
            updateListOfflineSubscription = routeMainInfoPojoObservable.subscribe(
                    routeMainInfoPojo -> mView.addDataToListAndUpdateAdapter(routeMainInfoPojo),
                    Throwable::printStackTrace,
                    () -> {
                        if (mView.getListItemsCount() == 0) {
                            if (InternetConnection.isOnline()) {
                                updateListOnline();
                            } else {
                                mView.showAlertNoInternet();
                            }
                        }
                    }
            );
            EventBus.getInstance().post(new RoutesListShowAlertEvent(null, false));
            EventBus.getInstance().post(new RoutesListShowRefreshingEvent(false));
        }
    }

    @Override
    public void releasePresenter() {
        EventBus.getInstance().unregister(this);
        if (updateListOfflineSubscription != null && !updateListOfflineSubscription.isUnsubscribed())
            updateListOfflineSubscription.unsubscribe();

        mView = null;

    }

    @Override
    public void onListItemClicked() {
        try {
            int clickedRouteId = mView.getDataByListPosition(mView.getListItemClickedPosition()).getId();
            Router.navigateToRouteInfoActivity(mView.getViewContext(), clickedRouteId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void updateListOnlineListener(RoutesListUpdateOnlineEvent event) {
        updateListOnline();
    }

    @Subscribe
    public void updateListOfflineListener(RoutesListUpdateOfflineEvent event) {
        updateListOffline();
    }

    private void saveToDatabase() {
            EventBus.getInstance().post(new RoutesListShowAlertEvent("Сохранение данных", true));

            Completable.create(completableSubscriber -> {
                EventBus.getInstance().post(new RoutesListSetIsCachingEvent(true));

                DataManager.cleanSavedRoutes(false);
                DataManager.saveRoutes(request[0]);

                EventBus.getInstance().post(new RoutesListSetIsCachingEvent(false));
                completableSubscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                EventBus.getInstance().post(new RoutesListShowAlertEvent(null, false));
                                EventBus.getInstance().post(new RoutesListUpdateOfflineEvent());
                            },
                            Throwable::printStackTrace
                    );
    }

    @Subscribe
    public void setIsCashingListener(RoutesListSetIsCachingEvent event) {
        mView.setIsCaching(event.isCaching());
    }

    @Subscribe
    public void setIsOnlineLoadingListener(RoutesListSetIsOnlineLoadingEvent event) {
        mView.setIsOnlineLoading(event.isOnlineLoading());
    }

    @Subscribe
    public void showNoDataListener(RoutesListShowNoDataEvent event) {
        if (event.isWillShown())
            mView.showNoDataText();
        else
            mView.hideNoDataText();
    }
}
