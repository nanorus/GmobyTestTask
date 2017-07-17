package com.example.nanorus.gmobytesttask.presenter;

import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.view.IRoutesListFragment;

import java.util.List;

import rx.Observable;

public class RoutesPresenter implements IRoutesPresenter {

    IRoutesListFragment mView;

    public RoutesPresenter(IRoutesListFragment view) {
        mView = view;
        updateListOnline(20140101, 20170501);
    }

    @Override
    public void updateListOnline(int fromDate, int toDate) {
        Observable<List<RouteMainInfoPojo>> routesMainInfos = DataManager.getRoutesMainInfoListOnline(fromDate, toDate);
        routesMainInfos.subscribe(
                routeMainInfoPojos -> mView.updateAdapter(routeMainInfoPojos),
                throwable -> System.out.println(throwable.getMessage()));
    }

    @Override
    public void updateListOffline() {

    }

    @Override
    public void releasePresenter() {

    }
}
