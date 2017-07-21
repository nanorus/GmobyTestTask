package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.app.Router;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.ShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.UpdateRoutesListEvent;
import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoutesListFragmentPresenter implements IRoutesListFragmentPresenter {

    private IRoutesListFragment mView;

    private Subscription updateListOnlineSubscription;
    private Subscription updateListOfflineSubscription;

    public RoutesListFragmentPresenter(IRoutesListFragment view) {
        mView = view;
        mView.createAndSetAdapter();
        updateListOffline();

        EventBus.getInstance().register(this);
    }

    @Override
    public void updateListOnline() {
        mView.showAlertLoading();
        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();
        final RequestPojo[] request = new RequestPojo[1];

        Observable<RequestPojo> requestPojoObservable = DataManager.loadRoutesOnline(20140101, 20170501)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        updateListOnlineSubscription = requestPojoObservable.subscribe(
                requestPojo -> {
                    try {
                        routeMainInfoPojos.addAll(DataConverter.convertFullRoutesListToMainInfoRouteList(requestPojo.getData()));
                        mView.updateAdapter(routeMainInfoPojos);
                        request[0] = requestPojo;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                },

                throwable -> System.out.println(throwable.getMessage()),

                () -> {
                    if (request[0].isSuccess()) {
                        if (request[0].getData().size() == 0)
                            mView.showNoDataText();
                        else
                            mView.hideNoDataText();
                        new Thread() {
                            @Override
                            public void run() {
                                DataManager.cleanSavedRoutes(false);
                                DataManager.saveRoutes(request[0], false);
                                mView.hideAlert();
                                super.run();
                            }
                        }.start();
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
        Observable<RouteMainInfoPojo> routeMainInfoPojoObservable = DataManager.loadRoutesMainInfoOffline(20140101, 20170501);
        updateListOfflineSubscription = routeMainInfoPojoObservable.subscribe(
                routeMainInfoPojo -> mView.addDataToListAndUpdateAdapter(routeMainInfoPojo),
                throwable -> System.out.println(throwable.getMessage()),
                () -> {
                    if (mView.getListItemsCount() == 0)
                        if (InternetConnection.isOnline()) {
                            updateListOnline();
                        } else {
                            mView.showAlertNoInternet();
                        }
                }
        );
    }

    @Override
    public void releasePresenter() {
        EventBus.getInstance().unregister(this);

        if (updateListOnlineSubscription != null && !updateListOnlineSubscription.isUnsubscribed())
            updateListOnlineSubscription.unsubscribe();
        if (updateListOfflineSubscription != null && !updateListOfflineSubscription.isUnsubscribed())
            updateListOfflineSubscription.unsubscribe();

        mView = null;
    }

    @Override
    public void onListItemClicked() {
        int clickedRouteId = mView.getDataByListPosition(mView.getListItemClickedPosition()).getId();
        Router.navigateToRouteInfoActivity(mView.getViewContext(), clickedRouteId);
    }

    @Subscribe
    public void updateListOnlineListener(UpdateRoutesListEvent event) {
        updateListOnline();
    }
}
