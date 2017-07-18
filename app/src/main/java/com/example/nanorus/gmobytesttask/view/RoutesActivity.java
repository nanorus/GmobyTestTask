package com.example.nanorus.gmobytesttask.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.bus.EventBus;
import com.example.nanorus.gmobytesttask.app.bus.event.ShowRefreshingEvent;
import com.example.nanorus.gmobytesttask.app.bus.event.UpdateRoutesListEvent;
import com.squareup.otto.Subscribe;

public class RoutesActivity extends AppCompatActivity implements IRoutesActivity {

    SwipeRefreshLayout activity_routes_swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        EventBus.getInstance().register(this);

        activity_routes_swipe = (SwipeRefreshLayout) findViewById(R.id.activity_routes_swipe);
        activity_routes_swipe.setOnRefreshListener(
                () -> {
                    EventBus.getInstance().post(new UpdateRoutesListEvent());
                }
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
    public IRoutesActivity getView() {
        return this;
    }

    @Subscribe
    public void ShowRefreshingListener(ShowRefreshingEvent event) {
        if (event.isShowRefresh())
            startShowRefreshing();
        else
            stopShowRefreshing();
    }

}
