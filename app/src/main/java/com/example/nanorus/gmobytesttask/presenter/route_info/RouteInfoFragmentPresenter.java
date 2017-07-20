package com.example.nanorus.gmobytesttask.presenter.route_info;

import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.view.route_info.IRouteInfoFragment;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RouteInfoFragmentPresenter implements IRouteInfoFragmentPresenter {

    IRouteInfoFragment mView;
    Subscription getRouteFullInfoSubscription;


    public RouteInfoFragmentPresenter(IRouteInfoFragment view, int id) {
        mView = view;

        Observable<DatumPojo> datumPojoObservable = DataManager.getRouteFullInfo(501)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        getRouteFullInfoSubscription = datumPojoObservable.subscribe(
                datumPojo -> {
                    // call mView showing methods
                    System.out.println(datumPojo.toString());
                },
                throwable -> {
                    throwable.printStackTrace();
                },
                () -> {
                });

    }

    @Override
    public void releasePresenter() {
        mView = null;
        if (!getRouteFullInfoSubscription.isUnsubscribed())
            getRouteFullInfoSubscription.unsubscribe();
    }
}
