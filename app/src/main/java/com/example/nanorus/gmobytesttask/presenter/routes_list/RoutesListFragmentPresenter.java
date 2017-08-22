package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.DataMapper;
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

    private Subscription mUpdateListOnlineSubscription;
    private Subscription mUpdateListOnlineServiceSubscription;
    private Subscription updateListOfflineSubscription;
    private Observable<RouteMainInfoPojo> routeMainInfoPojoObservable;
    private Single<RequestPojo> requestPojoSingle;
    private Single<Boolean> mIsServiceRequestSuccessfullSingle;
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
    private DataMapper mDataMapper;

    @Inject
    public RoutesListFragmentPresenter(ResourceManager resourceManager,
                                       RoutesListRouter routesListRouter, DataManager dataManager,
                                       InternetConnection internetConnection, DataMapper dataMapper) {
        mResourceManager = resourceManager;
        mRoutesListRouter = routesListRouter;
        mDataManager = dataManager;
        mInternetConnection = internetConnection;
        mDataMapper = dataMapper;
    }

    @Override
    public void updateListOnlineService() {
        isOnlineLoading = true;
        mIsServiceRequestSuccessfullSingle = mDataManager.loadRoutesOnlineFromService(mFromDate, mToDate);
        if (mUpdateListOnlineServiceSubscription != null && !mUpdateListOnlineServiceSubscription.isUnsubscribed()) {
            mUpdateListOnlineServiceSubscription.unsubscribe();
        }
        mView.showAlertLoading();

        mUpdateListOnlineServiceSubscription = mIsServiceRequestSuccessfullSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    isOnlineLoading = false;
                    if (aBoolean) {
                        mView.showAlertInsert();

                        Completable.create(completableSubscriber -> {
                            System.out.println("presenter: online loaded. start db input ");
                            mDataManager.saveRoutes(mDataManager.getRoutesRequestFromService());
                            completableSubscriber.onCompleted();
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    System.out.println("presenter: start offline load");
                                    updateListOffline();
                                    mView.hideAlert();
                                });
                    } else {
                        // error
                    }
                });

    }

    @Override
    public void updateListOnline() {
        isOnlineLoading = true;
        if (mUpdateListOnlineSubscription != null && !mUpdateListOnlineSubscription.isUnsubscribed()) {
            mUpdateListOnlineSubscription.unsubscribe();
        }

        mView.showAlertLoading();

        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();
        request = new RequestPojo[1];

        requestPojoSingle = mDataManager.loadRoutesOnline(mFromDate, mToDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mUpdateListOnlineSubscription = requestPojoSingle.subscribe(
                requestPojo -> {
                    try {
                        routeMainInfoPojos.addAll(mDataMapper.fullRoutesListToMainInfoRouteList(requestPojo.getData()));
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
        if (mUpdateListOnlineServiceSubscription != null && !mUpdateListOnlineServiceSubscription.isUnsubscribed())
            mUpdateListOnlineServiceSubscription.unsubscribe();
        if (mUpdateListOnlineSubscription != null && !mUpdateListOnlineSubscription.isUnsubscribed())
            mUpdateListOnlineSubscription.unsubscribe();
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
