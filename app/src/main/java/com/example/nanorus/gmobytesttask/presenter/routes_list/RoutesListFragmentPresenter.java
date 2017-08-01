package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.app.Router;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListSetIsCachingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListSetIsOnlineLoadingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowAlertEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowAlertRetryOnlineLoading;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListUpdateOfflineEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.routes_list.RoutesListUpdateOnlineEvent;
import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.presenter_base.Presenter;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoutesListFragmentPresenter implements IRoutesListFragmentPresenter, Presenter<IRoutesListFragment> {

    private IRoutesListFragment mView;

    private Subscription updateListOnlineSubscription;
    private Subscription updateListOfflineSubscription;
    private Observable<RouteMainInfoPojo> routeMainInfoPojoObservable;
    private Observable<RequestPojo> requestPojoObservable;
    private RequestPojo[] request;

    private int mFromDate = 20140101;
    private int mToDate = 20180501;

    private List<RouteMainInfoPojo> mData;

    private int count = 0;
    private boolean isPresenterCreated = false;
    private boolean isOnlineLoading = false;
    private boolean isOfflineSaving = false;

    public RoutesListFragmentPresenter() {
        EventBus.getInstance().register(this);
    }


    @Override
    public void updateListOnline() {
        isOnlineLoading = true;
        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed()) {
            updateListOnlineSubscription.unsubscribe();
        }

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
                    isOnlineLoading = false;
                    mView.hideAlert();
                    if (request[0].isSuccess()) {
                        if (request[0].getData().size() == 0)
                            showNoDataText(true);
                        else
                            showNoDataText(false);
                        saveToDatabase();
                    } else {
                        EventBus.getInstance().post(new RoutesListShowAlertEvent(App.getApp().getString(R.string.server_error), true));
                    }
                }
        );
    }

    private void showNoDataText(boolean willShown) {
        if (willShown)
            mView.showNoDataText();
        else
            mView.hideNoDataText();
    }

    @Override
    public void updateListOffline() {
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

    @Override
    public void releasePresenter() {
        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed())
            updateListOnlineSubscription.unsubscribe();
        if (updateListOfflineSubscription != null && !updateListOfflineSubscription.isUnsubscribed())
            updateListOfflineSubscription.unsubscribe();
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
        isOfflineSaving = true;
        System.out.println("set isOfflineSaving: true");
        mView.showAlertInsert();

        Completable.create(completableSubscriber -> {

            DataManager.cleanSavedRoutes(false);
            DataManager.saveRoutes(request[0]);

            EventBus.getInstance().post(new RoutesListSetIsCachingEvent(false));
            completableSubscriber.onCompleted();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            isOfflineSaving = false;
                            System.out.println("set isOfflineSaving: false");
                            mView.hideAlert();
                        },
                        Throwable::printStackTrace
                );
    }

    @Override
    public void onViewAttached(IRoutesListFragment view) {
        this.mView = view;
        this.count++;
        System.out.println("view atached " + this.count + " times");

        mView.createAndSetAdapter();

        if (!isPresenterCreated) {
            // one action for all presenter's lifecycle
            isPresenterCreated = true;
            updateListOffline();
        } else {
            // restoring saved data after configuration changed
            mView.fullUpdateAdapter(mData);
            mData.clear();
            mData = null;
            System.out.println("presenter: restoring data");
            if (isOfflineSaving) {
                mView.showAlertInsert();
                System.out.println("presenter: show alert insert");
            } else if (isOnlineLoading) {
                mView.showAlertLoading();
                System.out.println("presenter: show alert loading");
            }

        }

    }

    @Override
    public void onViewDetached() {
        // saving data
        if (mData == null)
            mData = mView.getData();
        else {
            mData.clear();
            mData.addAll(mView.getData());
        }

        this.mView = null;
    }

    @Override
    public void onDestroyed() {
        // releasing presenter
        releasePresenter();
        EventBus.getInstance().unregister(this);
    }
}
