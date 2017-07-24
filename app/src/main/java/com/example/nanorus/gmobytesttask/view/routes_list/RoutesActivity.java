package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesActivityPresenter;

public class RoutesActivity extends AppCompatActivity implements IRoutesActivity, RoutesListFragment.RoutesListEventListener {

    SwipeRefreshLayout activity_routes_swipe;
    RoutesActivityPresenter mPresenter;

    AlertDialog simpleAlert;


    private boolean mIsOnlineLoading = false;
    private static final String KEY_IS_ONLINE_LOADING = "IS_ONLINE_LOADING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        activity_routes_swipe = (SwipeRefreshLayout) findViewById(R.id.activity_routes_swipe);

        if (savedInstanceState != null) {
            mIsOnlineLoading = savedInstanceState.getBoolean(KEY_IS_ONLINE_LOADING);
            if (mIsOnlineLoading) {
                //  showAlert("Загрузка данных..");
                //    activity_routes_swipe.setEnabled(false);
            } else {
                //     activity_routes_swipe.setEnabled(true);
            }

        }

        mPresenter = new RoutesActivityPresenter(getView());
        EventBus.getInstance().register(this);

        activity_routes_swipe.setOnRefreshListener(() -> mPresenter.onRefresh());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_ONLINE_LOADING, mIsOnlineLoading);
    }

    @Override
    public void startShowRefreshing() {
        if (!activity_routes_swipe.isRefreshing())
            activity_routes_swipe.setRefreshing(true);
    }

    @Override
    public void stopShowRefreshing() {
        if (activity_routes_swipe.isRefreshing())
            activity_routes_swipe.setRefreshing(false);
        activity_routes_swipe.setEnabled(true);
    }

    @Override
    public void showAlertNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.no_internet))
                .setPositiveButton(R.string.alert_button_retry, (dialogInterface, i) -> mPresenter.onRefresh())
                .setOnDismissListener(DialogInterface::dismiss)
                .show();
    }

    @Override
    public IRoutesActivity getView() {
        return this;
    }

    @Override
    protected void onPause() {
        hideAlert();
        simpleAlert = null;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.releasePresenter();
        mPresenter = null;
        hideAlert();
        super.onDestroy();
    }


    @Override
    public void showAlertLoadFail(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(R.string.alert_button_retry, (dialogInterface, i) -> mPresenter.onRefresh())
                .setOnDismissListener(DialogInterface::dismiss)
                .show();

    }

    @Override
    public void showAlert(String message) {

        hideAlert();
        simpleAlert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        simpleAlert = builder.setMessage(message)
                .setCancelable(false)
                .show();
    }

    @Override
    public void hideAlert() {

        try {
            if (!this.isFinishing() && simpleAlert != null && simpleAlert.isShowing()) {
                simpleAlert.dismiss();
            }
        } catch (java.lang.IllegalArgumentException e) {

        }

    }


}
