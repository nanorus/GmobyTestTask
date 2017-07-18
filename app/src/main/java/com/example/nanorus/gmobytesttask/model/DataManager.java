package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.api.RoutesRetroClient;
import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.database.DatabaseManager;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;

import rx.Observable;

public class DataManager {

    public static Observable<RequestPojo> loadRoutesOnline(int fromDate, int toDate) {
        GetAllRoutersService service = RoutesRetroClient.getInstance().create(GetAllRoutersService.class);

        Observable<RequestPojo> routesFullInfoObservable = service.getAllRoutersRequestObservable(fromDate, toDate);
        return routesFullInfoObservable;
    }

    public static Observable<RequestPojo> loadRoutesMainInfoOffline(int fromDate, int toDate) {

        // load from db

        return null;
    }

    public static void saveRoutes(RequestPojo requestPojo) {
        DatabaseManager.putRoutes(requestPojo);
    }


}
