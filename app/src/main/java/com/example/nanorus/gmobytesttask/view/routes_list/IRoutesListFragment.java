package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;

import java.util.List;

public interface IRoutesListFragment {

    void createAndSetAdapter();

    void addDataToListAndUpdateAdapter(RouteMainInfoPojo routeMainInfoPojo);

    void updateAdapter(List<RouteMainInfoPojo> routeMainInfoPojos);

    void showSnackBarNoInternet();

    void showSnackBarServerError();

    void showNoDataText();

    void hideNoDataText();

    int getListItemsCount();

    IRoutesListFragment getViewLayer();

    Context getViewContext();

}
