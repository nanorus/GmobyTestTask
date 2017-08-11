package com.example.nanorus.gmobytesttask.view.route_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.presenter.route_info.IRouteInfoActivityPresenter;

import javax.inject.Inject;

public class RouteInfoActivity extends AppCompatActivity implements IRouteInfoActivity {

    @Inject
    IRouteInfoActivityPresenter mPresenter;

    private Toolbar route_info_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);


        route_info_toolbar = (Toolbar) findViewById(R.id.route_info_toolbar);
        setSupportActionBar(route_info_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        route_info_toolbar.setNavigationOnClickListener(view -> onBackPressed());

        Fragment routeInfoFragment = new RouteInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", getIntent().getIntExtra("id", 0));
        routeInfoFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frgmCont, routeInfoFragment);
        fragmentTransaction.commit();

        App.getApp().getRouteInfoComponent().inject(this);
        mPresenter.bindView(this);
    }


    @Override
    public IRouteInfoActivity getView() {
        return this;
    }

    @Override
    protected void onDestroy() {
        mPresenter.releasePresenter();
        mPresenter = null;
        if (this.isFinishing()){
            App.getApp().clearRouteInfoComponent();
        }
        super.onDestroy();
    }
}
