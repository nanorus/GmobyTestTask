package com.example.nanorus.gmobytesttask.presenter.routes_list;

import com.example.nanorus.gmobytesttask.view.routes_list.IRoutesListActivity;

public interface IRoutesListActivityPresenter {

    void onProfileClicked();

    void onRefresh();

    void releasePresenter();

    void bindView(IRoutesListActivity activity);

}
