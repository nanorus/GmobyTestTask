package com.example.nanorus.gmobytesttask.presenter.route_info;

import com.example.nanorus.gmobytesttask.model.DataConverter;
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

        Observable<DatumPojo> datumPojoObservable = DataManager.getRouteFullInfo(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        getRouteFullInfoSubscription = datumPojoObservable.subscribe(
                datumPojo -> {
                    // call mView showing methods
                    System.out.println(datumPojo.toString());
                    mView.setFromCityField(datumPojo.getFromCity().getName());
                    mView.setFromDateField(DataConverter.convertApiDateFormatToCorrectDateFormat(
                            datumPojo.getFromDate() + " " + datumPojo.getFromTime()));
                    mView.setFromInfoField(datumPojo.getFromInfo());
                    mView.setToCityField(datumPojo.getToCity().getName());
                    mView.setToDateField(DataConverter.convertApiDateFormatToCorrectDateFormat(
                            datumPojo.getToDate() + " " + datumPojo.getToTime()));
                    mView.setToInfoField(datumPojo.getToInfo());
                    mView.setPriceField(String.valueOf(datumPojo.getPrice()));
                    mView.setInfoField(datumPojo.getInfo());
                    mView.setBusIdField(String.valueOf(datumPojo.getBusId()));
                    mView.setReservationCountField(String.valueOf(datumPojo.getReservationCount()));
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
