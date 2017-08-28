package com.example.nanorus.gmobytesttask.view.routes_list;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.nanorus.gmobytesttask.App;
import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.presenter.routes_list.IRoutesListActivityPresenter;
import com.example.nanorus.gmobytesttask.view.route_info.RouteInfoFragment;

import javax.inject.Inject;

public class RoutesListActivity extends AppCompatActivity implements IRoutesListActivity, RoutesListFragment.RoutesListEventListener {

    private SwipeRefreshLayout activity_routes_swipe;
    private Button activity_routes_tb_btn_profile;

    @Inject
    IRoutesListActivityPresenter mPresenter;

    private RoutesListFragment routesListFragment;

    private AlertDialog simpleAlert;

    RouteInfoFragment routeInfoFragment;
    private boolean isInfoFragmentExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        activity_routes_tb_btn_profile = (Button) findViewById(R.id.activity_routes_tb_btn_profile);
        activity_routes_tb_btn_profile.setOnClickListener(view -> mPresenter.onProfileClicked());

        routesListFragment = (RoutesListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_routes_list);
        activity_routes_swipe = (SwipeRefreshLayout) findViewById(R.id.activity_routes_swipe);
        isInfoFragmentExist = findViewById(R.id.route_info_fragment_frame) != null;
        App.getApp().getRoutesListComponent().inject(this);
        mPresenter.bindView(this);

        activity_routes_swipe.setOnRefreshListener(() -> mPresenter.onRefresh());
    }

    private void showRouteInfoInFragment(int id) {
        routeInfoFragment = (RouteInfoFragment)
                getSupportFragmentManager().findFragmentById(R.id.route_info_fragment_frame);
        if (routeInfoFragment == null || routeInfoFragment.getRouteId() != id) {
            routeInfoFragment = RouteInfoFragment.newInstance(id);
            getSupportFragmentManager().beginTransaction().replace(R.id.route_info_fragment_frame, routeInfoFragment)
                    .commit();
        }
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
    public void showRouteInfoByPosition(int position) {
        int routeId = routesListFragment.getRouteByPosition(position).getId();
        showRouteInfoInFragment(routeId);
    }

    @Override
    public IRoutesListActivity getView() {
        return this;
    }

    @Override
    public Context getViewContext() {
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
        if (this.isFinishing()) {
            App.getApp().clearRoutesListComponent();
        }
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

    @Override
    public boolean isInfoFragmentExist() {
        return isInfoFragmentExist;
    }
}
