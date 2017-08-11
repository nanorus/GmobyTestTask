package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.ResourceManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.presenter.base.Presenter;
import com.example.nanorus.gmobytesttask.router.RoutesListRouter;
import com.example.nanorus.gmobytesttask.utils.InternetConnection;
import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoutesListFragmentPresenter implements IRoutesListFragmentPresenter, Presenter<IRoutesListFragment> {

    private IRoutesListFragment mView;

    private Subscription updateListOnlineSubscription;
    private Subscription updateListOfflineSubscription;
    private Observable<RouteMainInfoPojo> routeMainInfoPojoObservable;
    private Single<RequestPojo> requestPojoSingle;
    private RequestPojo[] request;

    private int mFromDate = 20140101;
    private int mToDate = 20180501;

    private List<RouteMainInfoPojo> mData;

    private boolean isPresenterCreated = false;
    private boolean isOnlineLoading = false;
    private boolean isOfflineSaving = false;

    private ResourceManager mResourceManager;
    private RoutesListRouter mRoutesListRouter;
    private DataManager mDataManager;
    private InternetConnection mInternetConnection;

    @Inject
    public RoutesListFragmentPresenter(ResourceManager resourceManager,
                                       RoutesListRouter routesListRouter, DataManager dataManager,
                                       InternetConnection internetConnection) {
        mResourceManager = resourceManager;
        mRoutesListRouter = routesListRouter;
        mDataManager = dataManager;
        mInternetConnection = internetConnection;
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

        requestPojoSingle = mDataManager.loadRoutesOnline(mFromDate, mToDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        updateListOnlineSubscription = requestPojoSingle.subscribe(
                requestPojo->{
                    try {
                        routeMainInfoPojos.addAll(DataConverter.convertFullRoutesListToMainInfoRouteList(requestPojo.getData()));
                        request[0] = requestPojo;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    isOnlineLoading = false;
                    mView.hideAlert();
                    if (request[0].isSuccess()) {
                        if (request[0].getData().size() == 0)
                            showNoDataText(true);
                        else
                            showNoDataText(false);
                        saveToDatabaseAndUpdateListOffline();
                    } else {
                        mView.showAlertRetryOnlineLoading(mResourceManager.getString(R.string.server_error));
                    }

                },
                throwable -> {
                    throwable.printStackTrace();
                    mView.hideAlert();
                    mView.showAlertFailLoading();
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
        routeMainInfoPojoObservable = mDataManager.loadRoutesMainInfoOffline(mFromDate, mToDate)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        ;
        updateListOfflineSubscription = routeMainInfoPojoObservable.subscribe(
                routeMainInfoPojo -> mView.addDataToListAndUpdateAdapter(routeMainInfoPojo),
                Throwable::printStackTrace,
                () -> {
                    if (mView.getListItemsCount() == 0) {
                        if (mInternetConnection.isOnline()) {
                            updateListOnline();
                        } else {
                            mView.showAlertNoInternet();
                        }
                    }
                }
        );
        mView.showSwipeRefreshing(false);
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
            mRoutesListRouter.navigateToRouteInfoActivity(mView.getViewContext(), clickedRouteId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void saveToDatabaseAndUpdateListOffline() {
        isOfflineSaving = true;
        mView.showAlertInsert();

        Completable.create(completableSubscriber -> {
            mDataManager.cleanSavedRoutes();
            mDataManager.saveRoutes(request[0]);
            completableSubscriber.onCompleted();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            isOfflineSaving = false;
                            mView.hideAlert();
                            mView.showSwipeRefreshing(false);
                            updateListOffline();
                        },
                        Throwable::printStackTrace
                );
    }

    @Override
    public void onViewAttached(IRoutesListFragment view) {
        this.mView = view;

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
            if (isOfflineSaving) {
                mView.showAlertInsert();
            } else if (isOnlineLoading) {
                mView.showAlertLoading();
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
    }
}
