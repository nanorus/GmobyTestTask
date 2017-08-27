package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.Context;

public interface IRoutesListActivity {

    void showAlert(String message);

    void hideAlert();

    void updateRoutesListOnline();

    void showAlertRetryOnlineLoading(String message);

    void showSwipeRefreshing(boolean willShow);

    IRoutesListActivity getView();

    Context getViewContext();

}
