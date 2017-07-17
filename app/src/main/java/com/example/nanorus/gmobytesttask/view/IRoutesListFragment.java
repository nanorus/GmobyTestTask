package com.example.nanorus.gmobytesttask.view;

import android.content.Context;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;

import java.util.List;

public interface IRoutesListFragment {

    void createAdapter();

    void setAdapter(List<RouteMainInfoPojo> routeMainInfoPojos);

    void updateAdapter(List<RouteMainInfoPojo> routeMainInfoPojos);

    IRoutesListFragment getViewLayer();

    Context getViewContext();

}
