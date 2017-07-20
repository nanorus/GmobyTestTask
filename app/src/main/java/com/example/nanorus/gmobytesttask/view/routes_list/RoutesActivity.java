package com.example.nanorus.gmobytesttask.view.routes_list;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesActivityPresenter;

public class RoutesActivity extends AppCompatActivity implements IRoutesActivity, RoutesListFragment.OnSnackBarEventListener {

    SwipeRefreshLayout activity_routes_swipe;
    RoutesActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        mPresenter = new RoutesActivityPresenter(getView());
        EventBus.getInstance().register(this);

        activity_routes_swipe = (SwipeRefreshLayout) findViewById(R.id.activity_routes_swipe);


        activity_routes_swipe.setOnRefreshListener(
                () -> mPresenter.onRefresh()
        );


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
    public void showSnackBarNoInternet() {
        Snackbar.make(this.findViewById(android.R.id.content), this.getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
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
    public void showSnackBar(String message, int duration) {
        Snackbar.make(this.findViewById(android.R.id.content), message, duration).show();
    }
}
