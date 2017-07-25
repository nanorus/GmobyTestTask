package com.example.nanorus.gmobytesttask.presenter.routes_list;

public interface IRoutesListFragmentPresenter {

    void updateListOnline();

    void updateListOffline();

    void onListItemClicked();

    void releasePresenter();

}