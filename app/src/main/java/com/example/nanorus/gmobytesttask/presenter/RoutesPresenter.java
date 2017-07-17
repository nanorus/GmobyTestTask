package com.example.nanorus.gmobytesttask.presenter;

import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.view.IRoutesListFragment;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class RoutesPresenter implements IRoutesPresenter {

    IRoutesListFragment mView;

    public RoutesPresenter(IRoutesListFragment view) {
        mView = view;


        Observable<List<RouteMainInfoPojo>> routesMainInfos = DataManager.getRoutesMainInfoListOnline(20140101, 20170501);
        routesMainInfos.subscribe(new Subscriber<List<RouteMainInfoPojo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onNext(List<RouteMainInfoPojo> routeMainInfoPojos) {

                // add to adapter

            }
        });


    }

    @Override
    public void updateListOnline() {

    }

    @Override
    public void updateListOffline() {

    }

    @Override
    public void releasePresenter() {

    }
}
