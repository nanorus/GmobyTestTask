package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.app.Router;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.RoutesListShowAlertEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.ShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.UpdateRoutesListOfflineEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.UpdateRoutesListOnlineEvent;
import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.PreferencesManager;
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

    InputIntoDBThread mUpdateOnlineDbInputThread;
    RequestPojo[] request;

    public RoutesListFragmentPresenter(IRoutesListFragment view) {
        mView = view;
        mView.createAndSetAdapter();
        System.out.println("Создан новый презентер");
        EventBus.getInstance().register(this);

        if (PreferencesManager.getIsIntoDBInserting()) {
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

        System.out.println("presenter: updateListOnline()");

        mView.showAlertLoading();
        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();
        request = new RequestPojo[1];

        requestPojoObservable = DataManager.loadRoutesOnline(mFromDate, mToDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        updateListOnlineSubscription = requestPojoObservable.subscribe(
                requestPojo -> {
                    try {
                        System.out.println("presenter: online: subscription: onNext()");
                        System.out.println("online: add all to list");
                        routeMainInfoPojos.addAll(DataConverter.convertFullRoutesListToMainInfoRouteList(requestPojo.getData()));
                        //    mView.updateAdapter(routeMainInfoPojos);
                        request[0] = requestPojo;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                },

                throwable -> System.out.println(throwable.getMessage()),

                () -> {
                    System.out.println("presenter: online: subscription: onCompleted()");
                    //     mView.hideAlert();
                    if (request[0].isSuccess()) {
                        if (request[0].getData().size() == 0)
                            mView.showNoDataText();
                        else
                            mView.hideNoDataText();
/*
                        if (mUpdateOnlineDbInputThread != null && mUpdateOnlineDbInputThread.isAlive())
                            mUpdateOnlineDbInputThread.interrupt();
*/

                        System.out.println("presenter: online: subscription: onCompleted: начинаем запись в бд");
                        saveToDatabase();

                        EventBus.getInstance().post(new ShowRefreshingEvent(false));
                        mView.showAlertInsert();
                    } else {
                        mView.showAlertServerError();
                    }
                }
        );
    }

    @Override
    public void updateListOffline() {
        System.out.println("presenter: updateListOffline()");
        if (PreferencesManager.getIsIntoDBInserting()) {
            System.out.println("presenter: updateListOffline canceled (db insert being)");
        } else {
            EventBus.getInstance().post(new RoutesListShowAlertEvent("Открытие", true));
            routeMainInfoPojoObservable = DataManager.loadRoutesMainInfoOffline(mFromDate, mToDate)
            // .subscribeOn(Schedulers.io())
            // .observeOn(AndroidSchedulers.mainThread())
            ;
            updateListOfflineSubscription = routeMainInfoPojoObservable.subscribe(
                    routeMainInfoPojo -> {
                        //  System.out.println("presenter: offline: subscr: next");
                        mView.addDataToListAndUpdateAdapter(routeMainInfoPojo);
                    },
                    throwable -> System.out.println(throwable.getMessage()),
                    () -> {
                        mView.updateAdapter(null);
                        System.out.println("presenter: offline: subscr: completed");
                        if (mView.getListItemsCount() == 0) {
                            if (InternetConnection.isOnline()) {
                                System.out.println("presenter: offline: subscr: empty list. now online loading");
                                updateListOnline();
                            } else {
                                mView.showAlertNoInternet();
                            }
                        }
                    }
            );
            EventBus.getInstance().post(new RoutesListShowAlertEvent(null, false));
        }
    }

    @Override
    public void releasePresenter() {
        System.out.println("presenter: releasePresenter");
        EventBus.getInstance().unregister(this);

        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed())
            updateListOnlineSubscription.unsubscribe();
        if (updateListOfflineSubscription != null && !updateListOfflineSubscription.isUnsubscribed())
            updateListOfflineSubscription.unsubscribe();

        if (mUpdateOnlineDbInputThread != null && mUpdateOnlineDbInputThread.isAlive())
            mUpdateOnlineDbInputThread.interrupt();


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
    public void updateListOnlineListener(UpdateRoutesListOnlineEvent event) {
        updateListOnline();
    }

    @Subscribe
    public void updateListOfflineListener(UpdateRoutesListOfflineEvent event) {
        updateListOffline();
    }

    private void saveToDatabase() {
        EventBus.getInstance().post(new RoutesListShowAlertEvent("Сохранение данных", true));

        Completable.create(completableSubscriber -> {
            System.out.println("into db (new thread): putting into db");
            if (!PreferencesManager.getIsIntoDBInserting()) {
                PreferencesManager.setIsIntoDBInserting(true);
                DataManager.cleanSavedRoutes(false);
                DataManager.saveRoutes(request[0], false);
                PreferencesManager.setIsIntoDBInserting(false);
                System.out.println("into db (new thread): putting completed");
                completableSubscriber.onCompleted();
            } else {
                completableSubscriber.onError(new Throwable("The insert to the database is already being"));
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            System.out.println("into db (main thread): putting completed");
                            EventBus.getInstance().post(new RoutesListShowAlertEvent(null, false));
                            EventBus.getInstance().post(new UpdateRoutesListOfflineEvent());
                        },
                        throwable -> throwable.printStackTrace()
                );
           /* if (mView != null) {
                mView.hideAlert();
                mView.setIsOnlineLoading(false);
            }*/
        /*
        mUpdateOnlineDbInputThread = new InputIntoDBThread();
        mUpdateOnlineDbInputThread.start();
        */
    }

    public class InputIntoDBThread extends Thread {
        @Override
        public void run() {

            super.run();
        }
    }

}
