package com.example.nanorus.gmobytesttask.presenter.route_info;

import com.example.nanorus.gmobytesttask.model.DataMapper;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.view.route_info.IRouteInfoFragment;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RouteInfoFragmentPresenter implements IRouteInfoFragmentPresenter {

    private IRouteInfoFragment mView;
    private Subscription getRouteFullInfoSubscription;

    private DataManager mDataManager;
    private DataMapper mDataMapper;

    @Inject
    RouteInfoFragmentPresenter(DataManager dataManager, DataMapper dataMapper) {
        mDataManager = dataManager;
        mDataMapper = dataMapper;
    }

    @Override
    public void loadAndShowData(int id) {
        Observable<DatumPojo> datumPojoObservable = mDataManager.getRouteFullInfo(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        getRouteFullInfoSubscription = datumPojoObservable.subscribe(
                datumPojo -> {
                    // call mView showing methods
                    mView.setFromCityField(datumPojo.getFromCity().getName());
                    mView.setFromDateField(mDataMapper.apiDateFormatToCorrectDateFormat(
                            datumPojo.getFromDate() + " " + datumPojo.getFromTime()));
                    mView.setFromInfoField(datumPojo.getFromInfo());
                    mView.setToCityField(datumPojo.getToCity().getName());
                    mView.setToDateField(mDataMapper.apiDateFormatToCorrectDateFormat(
                            datumPojo.getToDate() + " " + datumPojo.getToTime()));
                    mView.setToInfoField(datumPojo.getToInfo());
                    mView.setPriceField(String.valueOf(datumPojo.getPrice()));
                    mView.setInfoField(datumPojo.getInfo());
                    mView.setBusIdField(String.valueOf(datumPojo.getBusId()));
                    mView.setReservationCountField(String.valueOf(datumPojo.getReservationCount()));
                },
                Throwable::printStackTrace,
                () -> {
                });
    }

    @Override
    public void bindView(IRouteInfoFragment view) {
        mView = view;
    }

    @Override
    public void releasePresenter() {
        mView = null;
        if (!getRouteFullInfoSubscription.isUnsubscribed())
            getRouteFullInfoSubscription.unsubscribe();
    }
}
