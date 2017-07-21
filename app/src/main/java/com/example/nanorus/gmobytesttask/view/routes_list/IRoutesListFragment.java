package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;

import java.util.List;

public interface IRoutesListFragment {

    void createAndSetAdapter();

    void addDataToListAndUpdateAdapter(RouteMainInfoPojo routeMainInfoPojo);

    void updateAdapter(List<RouteMainInfoPojo> routeMainInfoPojos);

    void showNoDataText();

    void hideNoDataText();

    void showAlertNoInternet();

    void showAlertServerError();

    void showAlertLoading();

    void showAlertInsert();

    void hideAlert();

    int getListItemsCount();

    RouteMainInfoPojo getDataByListPosition(int position);

    int getListItemClickedPosition();

    IRoutesListFragment getViewLayer();

    Context getViewContext();

}
