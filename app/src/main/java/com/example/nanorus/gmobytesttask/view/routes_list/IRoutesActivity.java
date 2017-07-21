package com.example.nanorus.gmobytesttask.view.routes_list;

public interface IRoutesActivity {


    void startShowRefreshing();

    void stopShowRefreshing();

    void showAlertNoInternet();

    IRoutesActivity getView();

}
