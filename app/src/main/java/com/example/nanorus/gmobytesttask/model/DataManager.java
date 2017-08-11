package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.database.DatabaseManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Single;

@Singleton
public class DataManager {

    private DatabaseManager mDatabaseManager;
    private GetAllRoutersService mGetAllRoutersService;

    @Inject
    public DataManager(DatabaseManager databaseManager, GetAllRoutersService getAllRoutersService) {
        mDatabaseManager = databaseManager;
        mGetAllRoutersService = getAllRoutersService;
    }

    public Single<RequestPojo> loadRoutesOnline(int fromDate, int toDate) {
        return mGetAllRoutersService.getAllRoutersRequestObservable(fromDate, toDate);
    }

    public Observable<RouteMainInfoPojo> loadRoutesMainInfoOffline(int fromDate, int toDate) {
        return mDatabaseManager.getRoutesMainInfo(fromDate, toDate);
    }

    public Observable<DatumPojo> getRouteFullInfo(int routeId) {
        return mDatabaseManager.getRouteFullInfo(routeId);
    }

    public void saveRoutes(RequestPojo requestPojo) {
        mDatabaseManager.putRoutes(requestPojo);
    }

    public void cleanSavedRoutes() {
        mDatabaseManager.cleanSavedRoutes();
    }

}
