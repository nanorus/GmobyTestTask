package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesActivityPresenter;

public class RoutesActivity extends AppCompatActivity implements IRoutesActivity, RoutesListFragment.RoutesListEventListener {

    SwipeRefreshLayout activity_routes_swipe;
    RoutesActivityPresenter mPresenter;

    AlertDialog simpleAlert;

    Toolbar route_info_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        mPresenter = new RoutesActivityPresenter(getView());
        EventBus.getInstance().register(this);

        activity_routes_swipe = (SwipeRefreshLayout) findViewById(R.id.activity_routes_swipe);
        activity_routes_swipe.setOnRefreshListener(() -> mPresenter.onRefresh());

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
    protected void onDestroy() {
        mPresenter.releasePresenter();
        mPresenter = null;
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
        if (simpleAlert != null) {
            if (simpleAlert.isShowing()) {
                simpleAlert.setMessage(message);
            } else {
                simpleAlert.setMessage(message);
                simpleAlert.show();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            simpleAlert = builder.setMessage(message)
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    public void hideAlert() {
        if (simpleAlert != null) {
            simpleAlert.dismiss();
        }
    }
}
