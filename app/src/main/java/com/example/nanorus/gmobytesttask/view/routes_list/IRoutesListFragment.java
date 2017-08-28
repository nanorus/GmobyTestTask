package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;

import java.util.List;

public interface IRoutesListFragment {

    void createAndSetAdapter();

    void addDataToListAndUpdateAdapter(RouteMainInfoPojo routeMainInfoPojo);

    void fullUpdateAdapter(List<RouteMainInfoPojo> routeMainInfoPojos);

    List<RouteMainInfoPojo> getData();

    void showNoDataText();

    void hideNoDataText();

    void showAlertNoInternet();

    void showSwipeRefreshing(boolean willShow);

    void showAlertRetryOnlineLoading(String message);

    void showAlertLoading();

    void showAlertInsert();

    void hideAlert();

    int getListItemsCount();

    void showAlertFailLoading();

    RouteMainInfoPojo getDataByPositionAtList(int position);

    int getListItemClickedPosition();

    IRoutesListFragment getViewLayer();

    Context getViewContext();

}
