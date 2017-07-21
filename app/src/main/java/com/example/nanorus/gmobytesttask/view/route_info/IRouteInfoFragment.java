package com.example.nanorus.gmobytesttask.view.route_info;

import android.content.Context;

public interface IRouteInfoFragment {

    void setFromCityField(String text);

    void setToCityField(String text);

    void setFromDateField(String text);

    void setToDateField(String text);

    void setToInfoField(String text);

    void setFromInfoField(String text);

    void setPriceField(String text);

    void setInfoField(String text);

    void setBusIdField(String text);

    void setReservationCountField(String text);

    IRouteInfoFragment getViewLayer();

    Context getViewContext();
}
