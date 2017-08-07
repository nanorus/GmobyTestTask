package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListActivityPresenter;

public class RoutesListActivity extends AppCompatActivity implements IRoutesListActivity, RoutesListFragment.RoutesListEventListener {

    SwipeRefreshLayout activity_routes_swipe;
    RoutesListActivityPresenter mPresenter;

    RoutesListFragment routesListFragment;

    AlertDialog simpleAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        EventBus.getInstance().register(this);

        routesListFragment = (RoutesListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_routes_list);


        activity_routes_swipe = (SwipeRefreshLayout) findViewById(R.id.activity_routes_swipe);

        mPresenter = new RoutesListActivityPresenter(getView());

        activity_routes_swipe.setOnRefreshListener(() -> mPresenter.onRefresh());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void showAlertRetryOnlineLoading(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(R.string.alert_button_retry, (dialogInterface, i) -> mPresenter.onRefresh())
                .setOnDismissListener(DialogInterface::dismiss)
                .show();
    }

    @Override
    public void showSwipeRefreshing(boolean willShow) {
        if (willShow) {
            if (!activity_routes_swipe.isRefreshing())
                activity_routes_swipe.setRefreshing(true);
        } else {
            if (activity_routes_swipe.isRefreshing())
                activity_routes_swipe.setRefreshing(false);
        }
    }

    @Override
    public IRoutesListActivity getView() {
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
            e.printStackTrace();
        }
    }

    @Override
    public void updateRoutesListOnline() {
        routesListFragment.updateListOnline();
    }


}
