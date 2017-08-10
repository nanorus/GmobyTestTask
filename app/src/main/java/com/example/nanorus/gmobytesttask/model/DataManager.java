package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.database.DatabaseManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;

@Singleton
public class DataManager {

    private Retrofit mRoutesRetroClient;
    private DatabaseManager mDatabaseManager;

    @Inject
    public DataManager(DatabaseManager databaseManager, @Named("routesRetroClient") Retrofit routesRetroClient) {
        mDatabaseManager = databaseManager;
        mRoutesRetroClient = routesRetroClient;
    }

    public Observable<RequestPojo> loadRoutesOnline(int fromDate, int toDate) {
        GetAllRoutersService service = mRoutesRetroClient.create(GetAllRoutersService.class);
        return service.getAllRoutersRequestObservable(fromDate, toDate);
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
