package com.example.nanorus.gmobytesttask.view.routes_list;

public interface IRoutesActivity {

    void showAlert(String message);

    void hideAlert();

    void startShowRefreshing();

    void stopShowRefreshing();

    void showAlertNoInternet();

    IRoutesActivity getView();

}
