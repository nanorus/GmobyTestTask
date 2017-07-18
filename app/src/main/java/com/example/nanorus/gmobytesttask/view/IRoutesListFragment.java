package com.example.nanorus.gmobytesttask.view;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;

import java.util.List;

public interface IRoutesListFragment {

    void createAndSetAdapter();

    void addDataToListAndUpdateAdapter(RouteMainInfoPojo routeMainInfoPojo);

    void updateAdapter(List<RouteMainInfoPojo> routeMainInfoPojos);

    IRoutesListFragment getViewLayer();

    Context getViewContext();

}
