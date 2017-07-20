package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.api.RoutesRetroClient;
import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.database.DatabaseManager;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;

import rx.Observable;

public class DataManager {

    public static Observable<RequestPojo> loadRoutesOnline(int fromDate, int toDate) {
        GetAllRoutersService service = RoutesRetroClient.getInstance().create(GetAllRoutersService.class);

        Observable<RequestPojo> routesFullInfoObservable = service.getAllRoutersRequestObservable(fromDate, toDate);
        return routesFullInfoObservable;
    }

    public static Observable<RouteMainInfoPojo> loadRoutesMainInfoOffline(int fromDate, int toDate) {
        Observable<RouteMainInfoPojo> routeMainInfoPojoObservable = DatabaseManager.getRoutesMainInfo(fromDate, toDate);
        return routeMainInfoPojoObservable;
    }

    public static void saveRoutes(RequestPojo requestPojo, boolean inNewThread) {
        if (inNewThread) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    DatabaseManager.putRoutes(requestPojo);
                }
            };
            thread.start();
        } else
            DatabaseManager.putRoutes(requestPojo);
    }

    public static void cleanSavedRoutes(boolean inNewThread) {
        if (inNewThread) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    DatabaseManager.cleanSavedRoutes();
                }
            };
            thread.start();
        } else
            DatabaseManager.cleanSavedRoutes();
    }


}
