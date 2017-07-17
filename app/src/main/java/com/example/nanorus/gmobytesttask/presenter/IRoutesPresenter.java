package com.example.nanorus.gmobytesttask.presenter;

public interface IRoutesPresenter {

    void updateListOnline(int fromDate, int toDate);

    void updateListOffline();

    void releasePresenter();

}
