package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.api.RoutesRetroClient;
import com.example.nanorus.gmobytesttask.model.api.service.GetAllRoutersService;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataManager {

    public static Observable<List<RouteMainInfoPojo>> getRoutesMainInfoListOnline(int fromDate, int toDate) {
        GetAllRoutersService service = RoutesRetroClient.getInstance().create(GetAllRoutersService.class);

        Observable<List<RouteMainInfoPojo>> routesObservable = service.getAllRoutersRequestObservable(fromDate, toDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(requestPojo -> {
                    ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();
                    List<DatumPojo> datumPojos = requestPojo.getData();
                    for (int i = 0; i < datumPojos.size(); i++) {
                        DatumPojo datumPojo = datumPojos.get(i);
                        routeMainInfoPojos.add(
                                new RouteMainInfoPojo(
                                        datumPojo.getFromCity().getName(),
                                        datumPojo.getToCity().getName(),
                                        datumPojo.getFromDate(),
                                        datumPojo.getToDate(),
                                        datumPojo.getPrice()
                                )
                        );
                    }
                    return routeMainInfoPojos;
                });
                return routesObservable;
    }

}
